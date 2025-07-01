package com.r3edge.springflip;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlipBeanCondition implements Condition {


    /**
     * Évalue si le toggle est activé.
     *
     * @param context contexte Spring
     * @param metadata métadonnées de l’annotation
     * @return true si activé, sinon false
     */
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		// Récupère le nom du toggle depuis l'annotation
		String toggleName = (String) metadata.getAnnotationAttributes(FlipBean.class.getName()).get("value");

		// Lit la valeur du toggle dans l'environnement (par défaut : false)
		String toggleValue = context.getEnvironment().getProperty("spring.flip." + toggleName);
		boolean isEnabled = Boolean.parseBoolean(toggleValue != null ? toggleValue : "false");

		// Logge l'état du toggle en mode debug (pratique pour le débogage)
		log.debug("Evaluating feature flip '{}': {}", toggleName, isEnabled ? "ENABLED" : "DISABLED");

		return isEnabled; // Renvoie true ou false en fonction du toggle
	}
}
