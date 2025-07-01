package com.r3edge.springflip;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Écoute les changements d'environnement Spring Cloud et logge l'état des toggles.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SpringFlipCloudConfigToggleLogger implements ApplicationListener<EnvironmentChangeEvent> {

	private final Environment env;

    /**
     * Méthode appelée lors d'un changement d'environnement.
     *
     * @param event événement de changement
     */
	@Override
	public void onApplicationEvent(EnvironmentChangeEvent event) {
		event.getKeys().stream().filter(key -> key.startsWith("spring.flip.")).forEach(key -> {
			String featureName = key.substring("spring.flip.".length());
			boolean isEnabled = Boolean.parseBoolean(env.getProperty(key, "false"));
			log.info("Evaluating feature flip '{}': {}", featureName, isEnabled ? "ENABLED" : "DISABLED");
		});
	}
}
