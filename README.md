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
→ "FOOFOOFOO"

GET /transform/150
→ 400 Bad Request : "Number must be between 0 and 100, got: 150"
```

#### Choisir l'implémentation (V1 impératif / V2 déclaratif) :

Dans `application.properties` :
```properties
transformer.version=v1   # ou v2
```

Ou à l'exécution :
```bash
java -jar target/transformer-ns-0.0.1-SNAPSHOT.jar -Dtransformer.version=v2
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

| Classe de test | Description |
|---|---|
| `TransformerV1Test` | Tests unitaires de l'implémentation impérative |
| `TransformerV2Test` | Tests unitaires de l'implémentation déclarative |
| `TransformerControllerTest` | Tests du controller REST avec MockMvc |
| `TransformerItemProcessorTest` | Tests du processor batch avec mock |
| `BatchInputReaderTest` | Tests du reader batch |
| `BatchOutputWriterTest` | Tests du writer batch |

---

## 5. Architecture

```
src/main/java/.../
├── domain/                          ← métier pur, 0 Spring
│   ├── model/TransformResult.java
│   ├── port/in/TransformerService.java
│   ├── TransformerConstants.java
│   └── exception/InvalidNumberException.java
├── application/                     ← implémentations métier, 0 Spring
│   ├── TransformerV1.java           ← impératif (if/else)
│   └── TransformerV2.java           ← déclaratif (Stream)
├── adapter/
│   ├── in/
│   │   ├── web/TransformerController.java
│   │   └── batch/
│   │       ├── BatchJobConfig.java
│   │       ├── TransformerBatchRunner.java
│   │       ├── TransformerItemProcessor.java
│   │       ├── TransformerSkipPolicy.java
│   │       └── TransformerStepListener.java
│   └── out/file/
│       ├── BatchInputReader.java
│       └── BatchOutputWriter.java
└── config/TransformerConfig.java    ← choix V1 / V2
```
