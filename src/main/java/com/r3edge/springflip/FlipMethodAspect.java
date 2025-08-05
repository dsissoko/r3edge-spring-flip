package com.r3edge.springflip;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspect pour intercepter les méthodes annotées avec {@link FlipMethod}
 * et conditionner leur exécution selon l'état des toggles déclarés dans la configuration.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class FlipMethodAspect {

    private final FeatureRegistry featureRegistry;

    /**
     * Intercepte l'exécution d'une méthode annotée et vérifie l'état du toggle.
     *
     * @param joinPoint   point d'exécution
     * @param flipMethod  annotation {@link FlipMethod}
     * @return le résultat de la méthode ou null si la feature est désactivée
     * @throws Throwable si erreur d'exécution de la méthode
     */
    @Around("@annotation(flipMethod)")
    public Object checkFeatureToggle(ProceedingJoinPoint joinPoint, FlipMethod flipMethod) throws Throwable {
        String toggleName = flipMethod.value();
        boolean isEnabled = featureRegistry.isFeatureEnabled(toggleName);

        if (isEnabled) {
            log.debug("✅ Feature '{}' is ENABLED → executing: {}", toggleName, joinPoint.getSignature());
            return joinPoint.proceed();
        } else {
            log.warn("⛔ Feature '{}' is DISABLED → skipping: {}", toggleName, joinPoint.getSignature());

            Class<?> returnType = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature())
                    .getMethod().getReturnType();

            if (returnType.isPrimitive()) {
                Object fallback = getDefaultValueFor(returnType);
                log.warn("🟡 Méthode retourne un primitif : fallback = {}", fallback);
                return fallback;
            }

            return null;
        }
    }

    /**
     * Retourne une valeur par défaut pour un type primitif.
     * Exemple : 0 pour int, false pour boolean, etc.
     */
    private static Object getDefaultValueFor(Class<?> returnType) {
        try {
            return java.lang.reflect.Array.get(java.lang.reflect.Array.newInstance(returnType, 1), 0);
        } catch (Exception e) {
            log.error("❌ Impossible de créer fallback pour type primitif {}", returnType, e);
            return null;
        }
    }
}
