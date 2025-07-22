# r3edge-spring-flip | ![Logo](logo_ds.png)

Librairie Spring et spring Boot pour **activer ou désactiver dynamiquement des fonctionnalités** (feature toggles) à chaud, via configuration centralisée (ex : Spring Config Server).  
Permet de conditionner l'exécution de méthodes ou de beans sans redémarrage du système, avec une intégration élégante et peu invasive.

---

## ✅ Fonctionnalités

- Définition de **features** dans un fichier de configuration
- Annotation simple pour conditionner l'exécution de méthodes
- Evaluation dynamique à chaud (hot-refresh)
- Intégration fluide avec Spring Cloud Config + Bus
- Compatible avec des profils, environnements, stratégies custom
- Support de **feature flipping** sans besoin de champ `boolean`
- 📜 Documentation et audit possible via une interface dédiée (future UI)

---

## 🔧 Déclaration d'une feature (YAML)

Définition centralisée via Spring Config :

```yaml
r3edge:
  spring:
    flip:
      my-feature-key: true
      another-experimental-feature: false
```

| Clé Feature                  | Description                                 |
|-----------------------------|---------------------------------------------|
| `my-feature-key`            | Nom logique de la feature toggle            |
| `true` / `false`            | Active (`true`) ou désactive (`false`)      |

---

## 🧩 Activation via annotation

Annotez vos méthodes ou beans avec `@ConditionalOnFeature` :

```java
@ConditionalOnFeature("my-feature-key")
public void someExperimentalMethod() {
    // code exécuté uniquement si la feature est active
}
```

Cette annotation est évaluée dynamiquement et prend en compte les mises à jour de configuration à chaud.

---

## 🔁 Mise à jour à chaud

Compatible avec Spring Cloud Bus, les modifications apportées dans le Config Server peuvent être propagées immédiatement.

### Exemples :

| Cas                               | Effet                                         |
|----------------------------------|-----------------------------------------------|
| 🔄 Feature passée à `false`      | Méthode désactivée automatiquement            |
| 🆕 Nouvelle feature              | Active ou inactive selon valeur               |
| 🚫 Suppression d'une feature     | Considérée comme désactivée                   |

---

## 🧠 Architecture interne

Chaque appel passe par un **resolver centralisé** qui interroge le `FeatureRegistry`.  
Une interface SPI permet d’ajouter des stratégies personnalisées (par utilisateur, profil, date...).

---

## 📦 Compatibilité

✅ Testée avec :  
- **Spring Boot** `3.5.3`  
- **Spring Cloud** `2025.0.0`  
- **Java** `17` et `21`

🧘 Lib légère, sans dépendance transitive aux starters : fonctionne avec toute stack Spring moderne.  
Pas de `fat-jar`, pas de verrouillage.

---


## 🚀 Intégration

La lib est distribuée via GitHub Packages. Cette fonctionnalité manque encore un peu de maturité, voici la procédure “no-brain” pour intégrer **r3edge-spring-flip** (Gradle) en local comme en CI/CD.

### 1. Configurez votre PAT

**En local** (dans votre gradle.properties non versionné) :

```properties
# pour GitHub Packages (Maven)
gpr.user=votre_github_user_name
gpr.key=ghp_votrepat_read:packages
```

**ou** dans les secrets GITHUB pour du CI (car votre .properties ne doit pas être versionné):

```
GPR_KEY=ghp_votrepat_read:packages
```

### 2. Déclarer le dépôt et la dépendance dans votre `build.gradle`

```groovy
repositories {
  mavenCentral()
  maven {
    url = uri("https://maven.pkg.github.com/dsissoko/r3edge-spring-flip")
    credentials {
      username = project.findProperty("gpr.user")
                 ?: System.getenv("GPR_USER")
                 ?: System.getenv("GITHUB_ACTOR")
      password = project.findProperty("gpr.key")
                 ?: System.getenv("GPR_KEY")
                 ?: System.getenv("GITHUB_TOKEN")
    }
  }
}

dependencies {
    implementation "com.r3edge:r3edge-spring-flip:0.1.1"
    implementation "org.springframework.boot:spring-boot-starter"
}
```

### 3. Exemple de CI pour GitHub Actions

```yaml
name: CI – Build & Publish

on:
  push:
    branches: 
      - main
    tags:
      - 'v*.*.*'
  pull_request:
    branches:
      - main

permissions:
  contents: read     # checkout + lecture de code
  packages: write    # nécessaire pour read & write packages

jobs:
  build-and-publish:
    runs-on: ubuntu-latest
    
    env:
      GPR_USER: ${{ secrets.GPR_USER }}
      GPR_KEY:  ${{ secrets.GPR_KEY }}

    steps:
      - uses: actions/checkout@v3
      
      - name: Make Gradle wrapper executable
        run: chmod +x ./gradlew

      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          distribution: temurin
          java-version: '21'
          cache: gradle

      - name: Build and Test
        run: ./gradlew clean build --no-daemon

      - name: Publish to GitHub Packages
        if: startsWith(github.ref, 'refs/tags/')
        run: ./gradlew publish --no-daemon

```

### 4. Lancez votre build

```bash
./gradlew clean build
```

---

## 📌 Exemple sur une méthode

déclarer les features dans le yaml de config (en local ou dans une repogit servie par spring config server):

```
r3edge:
  spring:
    # --- Feature Toggle (SpringFlip) ---
    flip:
      strategy.enable.short-selling: false
```    
      

```java
@Component
public class TradingStrategyService {

    @ConditionalOnFeature("strategy.enable.short-selling")
    public void evaluateShortEntry() {
        // Code conditionnel à l’activation de la vente à découvert
    }
}
```

---

## 📦 Roadmap

- [ ] UI d’administration des toggles (avec WebSocket + Spring Actuator)
- [ ] Annotations alternatives comme `@FeatureToggle("key")`
- [ ] Historisation des activations par utilisateur / environnement

---

[![Build and Test - r3edge-spring-flip](https://github.com/dsissoko/r3edge-spring-flip/actions/workflows/cicd_code.yml/badge.svg)](https://github.com/dsissoko/r3edge-spring-flip/actions/workflows/cicd_code.yml)
