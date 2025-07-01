package com.r3edge.springflip.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


/**
 * Configuration automatique du dispatcher de tâches.
 * Active la configuration des propriétés et scanne les composants nécessaires.
 */
@AutoConfiguration
@ComponentScan(basePackages = "com.r3edge.springflip")
public class SpringFlipAutoConfiguration {}