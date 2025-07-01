package com.r3edge.springflip;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import lombok.extern.slf4j.Slf4j;

/**
 * Condition personnalisée utilisée pour activer ou désactiver l'enregistrement d'un bean
 * en fonction d'un toggle de fonctionnalité défini dans la configuration de l'application.
 * <p>
 * Cette condition est généralement utilisée avec {@link org.springframework.context.annotation.Conditional}
 * pour contrôler la création de beans selon l'état d'un flag {@code spring.flip.<feature>} à {@code true}.
 * </p>
 *
 * @see org.springframework.context.annotation.Conditional
 */
@Slf4j
public class FlipBeanCondition implements Condition {


	/**
	 * Évalue si le bean doit être enregistré selon la valeur du toggle de fonctionnalité
	 * dans l'environnement de configuration.
	 *
	 * @param context contexte conditionnel fourni par Spring
	 * @param metadata métadonnées de l'annotation déclarée
	 * @return {@code true} si la fonctionnalité est activée, {@code false} sinon
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
