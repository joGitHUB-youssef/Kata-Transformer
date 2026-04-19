# Transformer Kata — Contexte de session

## Objectif
Kata Spring Boot : transformer un nombre (0-100) en chaîne selon des règles métier.
Exposé via REST API + Batch (fichier input.txt → output.txt).

## Règles métier
- Divisible par 3 → "FOO" (prioritaire sur "contient")
- Divisible par 5 → "BAR" (prioritaire sur "contient")
- Contient le chiffre 3 → "FOO"
- Contient le chiffre 5 → "BAR"
- Contient le chiffre 7 → "QUIX"
- Analyse de gauche à droite, divisible > contient
- Aucune règle → retourner le nombre en string
- Deux implémentations attendues : TransformerV1 (impératif) et TransformerV2 (déclaratif)

## Architecture hexagonale retenue

```
transformer_ns/
├── domain/                          ← métier pur, 0 Spring
│   ├── model/TransformResult.java   ← number + result (utilisé par le batch)
│   ├── port/
│   │   ├── in/TransformerUseCase.java   ← interface transform(int) : String
│   │   └── out/FilePort.java            ← interface readLines / writeLine
│   └── exception/InvalidNumberException.java
├── application/                     ← implémentations métier, 0 Spring
│   ├── TransformerV1.java           ← implémente TransformerUseCase (impératif)
│   └── TransformerV2.java           ← implémente TransformerUseCase (déclaratif)
├── adapter/
│   ├── in/
│   │   ├── web/TransformerController.java       ← REST controller
│   │   └── batch/TransformerBatchRunner.java    ← CommandLineRunner
│   └── out/
│       └── file/
│           ├── BatchInputReader.java
│           └── BatchOutputWriter.java
└── TransformerNsApplication.java
```

## Etat d'avancement

### Fait
- [x] Structure des dossiers et fichiers créés
- [x] domain/model/TransformResult.java
- [x] domain/port/in/TransformerUseCase.java
- [x] domain/port/out/FilePort.java
- [x] domain/exception/InvalidNumberException.java

### A faire
- [ ] application/TransformerV1.java
- [ ] application/TransformerV2.java
- [ ] adapter/in/web/TransformerController.java
- [ ] adapter/in/batch/TransformerBatchRunner.java
- [ ] adapter/out/file/BatchInputReader.java
- [ ] adapter/out/file/BatchOutputWriter.java

## Question ouverte
TransformResult est dans domain/model/ mais sert uniquement au batch.
A discuter : le garder dans domain/model/ ou le déplacer dans adapter/in/batch/ ?

## Lancement batch
```bash
java -jar transformer.jar --batch=true --input=input.txt --output=output.txt
```
