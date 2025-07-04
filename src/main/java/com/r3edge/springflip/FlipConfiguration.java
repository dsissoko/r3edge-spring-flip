package com.r3edge.springflip;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Représente la configuration externe des fonctionnalités (feature toggles) de l'application.
 * <p>
 * Cette classe est automatiquement liée aux propriétés définies sous le préfixe
 * {@code r3edge.spring.flip} dans le fichier {@code application.yml}, {@code application.properties}
 * ou via un serveur de configuration externe (Spring Cloud Config, Vault, etc.).
 * </p>
 *
 * @see org.springframework.boot.context.properties.ConfigurationProperties
 */
@RefreshScope
@ConfigurationProperties(prefix = "r3edge.spring")
@Slf4j
public class FlipConfiguration {

    private Map<String, Boolean> flip;

    /**
     * Retourne la map des fonctionnalités activées ou désactivées.
     *
     * @return une map des toggles de fonctionnalité ({@code featureName → enabled})
     */
    
    public Map<String, Boolean> getFlip() {
        return flip;
    }

    /**
     * Définit la map des fonctionnalités activées ou désactivées.
     *
     * @param flip une map représentant les toggles de fonctionnalité ({@code featureName → enabled})
     */

    public void setFlip(Map<String, Boolean> flip) {
        this.flip = flip;
    }
    
    /**
     * Méthode appelée après l'initialisation du bean Spring.
     * Logge le nombre de toggles de fonctionnalités chargés depuis la configuration.
     */
    @PostConstruct
    public void postConstruct() {
        if (flip != null && !flip.isEmpty()) {
            flip.forEach((key, value) ->
                log.info("📦 Feature toggle '{}' initialisé à : {}", key, value ? "ENABLED" : "DISABLED")
            );
        } else {
            log.warn("⚠️ Aucun toggle de fonctionnalité trouvé dans la configuration.");
        }
    }
}
