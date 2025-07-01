package com.r3edge.springflip;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour conditionner l’exécution d’une méthode en fonction d’un
 * toggle.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlipMethod {
    /**
     * Nom du toggle à vérifier.
     *
     * @return le nom du toggle
     */
	String value();
}
