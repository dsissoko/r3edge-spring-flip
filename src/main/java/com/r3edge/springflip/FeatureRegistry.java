package com.r3edge.springflip;

import java.util.Map;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service centralisé permettant d'accéder dynamiquement à l'état des fonctionnalités
 * (feature toggles) définies dans la configuration de l'application.
 * <p>
 * Ce composant est compatible avec le rechargement à chaud via {@code /actuator/refresh}
 * lorsqu'il est combiné à une source de configuration dynamique (YAML, Spring Cloud Config, etc.).
 * </p>
 */
@RefreshScope
@Component
@RequiredArgsConstructor
@Slf4j
public class FeatureRegistry {

	private final FlipConfiguration config;

	/**
	 * Vérifie si une fonctionnalité identifiée par son nom est activée.
	 *
	 * @param name le nom de la fonctionnalité à vérifier
	 * @return {@code true} si la fonctionnalité est activée, {@code false} sinon
	 */

	public boolean isFeatureEnabled(String name) {
        boolean enabled = config.getFlip().getOrDefault(name, false);
        log.debug("🪄 Feature '{}' = {}", name, enabled ? "ENABLED" : "DISABLED");
        return enabled;
	}
}
