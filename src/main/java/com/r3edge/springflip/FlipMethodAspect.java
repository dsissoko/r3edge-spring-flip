package com.r3edge.springflip;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class FlipMethodAspect {

	private final Environment environment;

	public FlipMethodAspect(Environment environment) {
		this.environment = environment;
		log.info("FlipMethodAspect initialized.");
	}

	@Around("@annotation(featureToggle)")
	public Object checkFeatureToggle(ProceedingJoinPoint joinPoint, FlipMethod flipMethod) throws Throwable {
		// Récupère le nom du toggle depuis l'annotation
		String toggleName = flipMethod.value();

		// Lit la valeur du toggle dans l'environnement (par défaut : false)
		boolean isEnabled = Boolean.parseBoolean(environment.getProperty("spring.flip." + toggleName, "false"));

		if (isEnabled) {
			// Si activé, exécute la méthode et logge l'action
			 log.debug("Feature flip '{}' is ENABLED. Proceeding with method: {}",
			 toggleName, joinPoint.getSignature());
			return joinPoint.proceed();
		} else {
			// Si désactivé, logge et retourne null (ou une autre valeur par défaut si
			// besoin)
			 log.debug("Feature flip '{}' is DISABLED. Skipping method: {}", toggleName,
			 joinPoint.getSignature());
			return null;
		}
	}
}
