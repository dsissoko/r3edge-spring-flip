# r3edge-spring-flip | ![Logo](logo_ds.png)

Librairie Spring Boot pour **activer ou désactiver dynamiquement des fonctionnalités** (feature toggles) à chaud, via configuration centralisée (ex : Spring Config Server).  
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
  features:
    toggles:
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

## 🚀 Intégration

Ajoutez la dépendance dans votre projet (exemple avec Gradle) :

```groovy
dependencies {
    implementation "com.r3edge:spring-flip:1.0.0"
}
```

Assurez-vous d'activer `Spring Cloud Config` et `@RefreshScope` dans vos beans.

---

## 📌 Exemple complet

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

## 🧪 Tests

Les features peuvent être forcées en test via une configuration de profil ou un mock du registry :

```java
featureRegistry.enable("strategy.enable.short-selling");
```

---

## 📦 Roadmap

- [ ] UI d’administration des toggles (avec WebSocket + Spring Actuator)
- [ ] Annotations alternatives comme `@FeatureToggle("key")`
- [ ] Historisation des activations par utilisateur / environnement

---

[![Build and Test - r3edge-spring-flip](https://github.com/dsissoko/r3edge-spring-flip/actions/workflows/cicd_code.yml/badge.svg)](https://github.com/dsissoko/r3edge-spring-flip/actions/workflows/cicd_code.yml)
