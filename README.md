# r3edge-spring-flip | ![Logo](logo_ds.png)

Librairie spring Boot de feature togggle déclarative. **Activer ou désactiver dynamiquement des fonctionnalités** (feature toggles) en mettant à jour le fichier application.yml de votre microservice ou de votre web app. Toogle de Bean avec **@FlibBean("feature1")** ou de méthode avec **@FlibMethod("feature2")**.

> 🚀 Pourquoi adopter `r3edge-spring-flip` ?
>
> ✅ 1 **API triviale** : Définissez vos feature en YAML, Annoter vos bean et vos méthodes flippables.  
> ✅ **Hot reload** des features  
> ✅ 100 % compatible **Spring Boot 3.x**  

This project is documented in French 🇫🇷 by default.  
An auto-translated English version is available here:

[👉 English (auto-translated by Google)](https://translate.google.com/translate?sl=auto&tl=en&u=https://github.com/dsissoko/r3edge-task-dispatcher)

---

## 📋 Fonctionnalités clés

- ✅ Définition de **features** dans un fichier de configuration yml
- ✅ Annotation simple pour conditionner l'exécution de méthodes ou l'activation des beans
- ✅ Support du **@RefreshScope** des features (Config Server + Spring Cloud Bus)
- ✅ Intégration fluide avec Spring Cloud Config + Bus

---

## ⚙️ Intégration rapide

### Ajouter les dépendances nécessaires:

```groovy
repositories {
    mavenCentral()
    // Dépôt GitHub Packages de r3edge-spring-flip
    maven {
        url = uri("https://maven.pkg.github.com/dsissoko/r3edge-spring-flip")
        credentials {
            username = ghUser
            password = ghKey
        }
    }
    mavenLocal()
}

dependencies {
    ....
    implementation "com.r3edge:r3edge-spring-flip:0.1.1"
    implementation "org.springframework.boot:spring-boot-starter"
    ...
}
```

> ⚠️ Cette librairie est publiée sur **GitHub Packages**: Même en open source, **GitHub impose une authentification** pour accéder aux dépendances.  
> Il faudra donc valoriser ghUser et ghKey dans votre gradle.properties:

```properties
#pour réccupérer des packages github 
ghUser=your_github_user
ghKey=github_token_with_read_package_scope
```

### Déclarez vos features dans la configuration yaml de votre microservice:

```yaml
r3edge:
  spring:
    flip:
      bean1: true
      method1: true
```

---

### Annotez vos méthodes ou beans:

```java
package com.example.demo;

import org.springframework.stereotype.Service;

import com.r3edge.springflip.FlipBean;
import com.r3edge.springflip.FlipMethod;

import lombok.extern.slf4j.Slf4j;

@Service
@FlipBean("bean1")
@Slf4j
public class FlippableBeanWithFlippableMethods {

    @FlipMethod("method1")
    public void flippableMethod(String data) {
        log.info("{} : No, im not flipped", data);
    }
}
```

> **Deux postures :**
>
> - **Fail Fast** 🚨 : Crash immédiat si une feature flippée est absente: les dev ne peuvent pas utiliser les features flippées
>   ```java
>   @Autowired
>   private FlippableBeanWithFlippableMethods bean1;
>   // Crash si bean1 désactivé
>   bean1.flippableVoid("critical");
>   String res = bean1.flippableObject("critical"); // Crash si méthode désactivée
>   ```
>
> - **Feature Aware** 🛡️ : Code robuste, fonctionne même si la feature est absente: les dev doivent coder le comportement en cas de feature flippée
>   ```java
>   private final Optional<FlippableBeanWithFlippableMethods> bean1;
>   // Pas de crash, exécution conditionnelle
>   bean1.ifPresent(b -> b.flippableVoid("optional"));
>   // Pour une méthode qui retourne une valeur :
>   String res = bean1.map(b -> b.flippableObject("optional")).orElse(null);
>   ```
>
> 🟢 **r3edge-spring-flip supporte les deux !**


> ℹ️ Les annotations sont évaluées au runtime via AOP et prennent en compte les mises à jour de configuration à chaud  



| Cas                               | Effet                                         |
|----------------------------------|-----------------------------------------------|
| 🔄 Feature passée à `false`      | Méthode désactivée automatiquement            |
| 🆕 Nouvelle feature              | Active ou inactive selon valeur               |
| 🚫 Suppression d'une feature     | Considérée comme désactivée                   |

---

## 📦 Stack de référence

✅ Testée avec :  
- **Spring Boot** `3.5.3`  
- **Spring Cloud** `2025.0.0`  
- **Java** `17` et `21`

---

## 📦 Roadmap

### 🔧 À venir

- RAS

### 🧠 En réflexion

- [ ] UI d’administration des toggles
- [ ] Historisation des activations par utilisateur / environnement

---

📫 Maintenu par [@dsissoko](https://github.com/dsissoko) – contributions bienvenues.

[![Build and Test - r3edge-spring-flip](https://github.com/dsissoko/r3edge-spring-flip/actions/workflows/cicd_code.yml/badge.svg)](https://github.com/dsissoko/r3edge-spring-flip/actions/workflows/cicd_code.yml)
