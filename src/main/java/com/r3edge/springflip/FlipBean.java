package com.r3edge.springflip;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * Annotation pour activer ou désactiver un bean/class en fonction d’un toggle.
 * Le toggle est défini dans l’environnement Spring.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(FlipBeanCondition.class)
public @interface FlipBean {
	/**
	 * Nom du toggle à vérifier.
	 */
	String value();
}
