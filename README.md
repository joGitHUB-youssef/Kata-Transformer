# Kata - API & Batch Spring Boot

Ce projet Spring Boot implémente la transformation de nombres (0-100) en chaîne de caractères selon des règles métier.
Il expose deux fonctionnalités distinctes :

- **API REST** — `GET /transform/{number}`
- **Batch** — traitement d'un fichier `input.txt` → `output.txt`

---

## Prérequis

- Java 17+
- Maven 3.8+
- Spring Boot 3.4.1
- Spring Batch 5.x

---

## Règles métier

| Règle | Résultat |
|---|---|
| Divisible par 3 | `FOO` |
| Divisible par 5 | `BAR` |
| Contient le chiffre 3 | `FOO` |
| Contient le chiffre 5 | `BAR` |
| Contient le chiffre 7 | `QUIX` |
| Aucune règle | le nombre en string |

> La règle "divisible" est prioritaire (appliquée en premier), puis les chiffres sont analysés de gauche à droite.

**Exemples :**
```
33  → FOOFOOFOO   (÷3 + chiffre 3 + chiffre 3)
15  → FOOBARBAR   (÷3 + ÷5 + chiffre 5)
37  → FOOQUIX     (chiffre 3 + chiffre 7)
4   → 4           (aucune règle)
```

---

## 1. Build du projet

```bash
# Compiler
./mvnw compile

# Compiler + lancer les tests
./mvnw test

# Générer le jar
./mvnw package -DskipTests
```

---

## 2. Lancer l'API REST

```bash
# Via Maven
./mvnw spring-boot:run

# Via le jar
java -jar target/transformer-ns-0.0.1-SNAPSHOT.jar
```

#### Exemple d'appel :
```bash
GET /transform/33
→ 200 : "FOOFOOFOO"

GET /transform/150
→ 400 : { "message": "Number must be between 0 and 100, got: 150", "status": 400 }

GET /transform/abc
→ 400 : { "message": "Le paramètre doit être un entier valide, reçu : abc", "status": 400 }

GET /transform
→ 400 : { "message": "Le paramètre number est obligatoire. Usage : /transform/{number}", "status": 400 }
```

---

## 3. Lancer le Batch

#### Préparer le fichier d'entrée :
```bash
echo -e "10\n33\n15\n7\nabc\n150\n4" > input.txt
```

#### Via Maven :
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=batch
```

#### Via le jar :
```bash
java -jar target/transformer-ns-0.0.1-SNAPSHOT.jar --spring.profiles.active=batch
```

#### Avec des chemins personnalisés :
```bash
java -jar target/transformer-ns-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=batch \
    --batch.input-file=file:./custom/input.txt \
    --batch.output-file=file:./custom/output.txt
```

#### Résultat attendu dans `output.txt` :
```
10 "BAR"
33 "FOOFOOFOO"
15 "FOOBARBAR"
7 "QUIX"
4 "4"
```
> Les lignes invalides (`abc`, `150`) sont skippées. Un rapport s'affiche dans les logs avec le nombre de lignes traitées et ignorées.

#### Configuration via `application.properties` :
```properties
batch.input-file=file:input.txt
batch.output-file=file:output.txt
```

---

## 4. Tests

```bash
# Tous les tests
./mvnw test

# Un test spécifique
./mvnw test -Dtest="TransformerV1Test"
```

### Tests unitaires

| Classe | Description |
|---|---|
| `TransformerV1Test` | Implémentation impérative — retourne la chaîne transformée |
| `TransformerV2Test` | Implémentation batch — retourne la ligne formatée pour output.txt |
| `TransformerOptimizedTest` | Implémentation avec pré-calcul + vérifie les 101 valeurs |
| `TransformerControllerTest` | Controller REST avec `@MockitoBean` + MockMvc |
| `TransformerItemProcessorTest` | Processor batch : délègue à `TransformerV2` via `@Qualifier` |
| `BatchInputReaderTest` | Reader batch (fichier valide, vide, invalide, hors plage) |
| `BatchOutputWriterTest` | Writer batch : écrit directement la ligne formatée produite par V2 |

### Tests d'intégration

| Classe | Description |
|---|---|
| `BatchIntegrationTest` | Lance le vrai job Spring Batch sur un fichier temporaire, vérifie le statut `COMPLETED`, le contenu de `output.txt` et le nombre de lignes skippées |

---

## 5. Architecture

```
src/main/java/.../
├── domain/                              ← métier pur, 0 Spring
│   ├── model/TransformResult.java       ← encapsule number + result (utilisé par le batch)
│   ├── port/in/TransformerService.java  ← interface transform(int) : String
│   ├── TransformerConstants.java        ← FOO, BAR, QUIX
│   └── exception/InvalidNumberException.java
├── application/                         ← implémentations, toutes @Component
│   ├── TransformerV1.java               ← impératif (if/else + for) — @Component
│   ├── TransformerV2.java               ← batch output file (Stream + Record) — @Component @Qualifier("batchTransformer")
│   └── TransformerOptimized.java        ← pré-calcul des 101 valeurs — @Component @Primary (REST)
├── adapter/
│   ├── in/
│   │   ├── web/
│   │   │   ├── TransformerController.java      ← GET /transform/{number} — injecte @Primary (TransformerOptimized)
│   │   │   ├── GlobalExceptionHandler.java     ← @RestControllerAdvice, gestion centralisée des erreurs
│   │   │   └── ErrorResponse.java              ← réponse JSON pour les erreurs
│   │   └── batch/
│   │       ├── BatchJobConfig.java          ← configuration Job + Step Spring Batch
│   │       ├── TransformerBatchRunner.java  ← CommandLineRunner, lance le job
│   │       ├── TransformerItemProcessor.java← Integer → TransformResult — injecte @Qualifier("batchTransformer") (TransformerV2)
│   │       ├── TransformerSkipPolicy.java   ← lignes invalides skippées sans arrêt
│   │       └── TransformerStepListener.java ← rapport en fin de job
│   └── out/file/
│       ├── BatchInputReader.java            ← lit input.txt ligne par ligne
│       └── BatchOutputWriter.java          ← écrit directement la ligne formatée produite par TransformerV2
```

### Sélection des implémentations

| Bean | Annotation | Utilisé par |
|---|---|---|
| `TransformerOptimized` | `@Primary` | `TransformerController` (REST) |
| `TransformerV2` | `@Qualifier("batchTransformer")` | `TransformerItemProcessor` (Batch) |
| `TransformerV1` | `@Component` | délégué interne de `TransformerOptimized` |
