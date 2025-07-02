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
            log.debug("⛔ Feature '{}' is DISABLED → skipping: {}", toggleName, joinPoint.getSignature());
         // TODO: revoir gestion fallback future
            return null; // ou un fallback configurable si tu le souhaites plus tard
        }
    }
}
