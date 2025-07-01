package com.r3edge.springflip;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.context.refresh.ContextRefresher;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = TestApplication.class)
@ExtendWith(OutputCaptureExtension.class)
@Slf4j
public class SpringFlipIntegrationTest {

	@Autowired
	private FlipConfiguration flipConfiguration;

	@Autowired
	private ContextRefresher contextRefresher;
	
	@Autowired(required = false)
	private FlippedBean flippedBean;
	
	@Autowired
	private BeanWithFlippedMethod beanWithFlippedMethod;
	
    @Autowired
    private MultiFeatureService service;

	@Test
	void shouldLoadFeaturesFromYaml() {
		Map<String, Boolean> flips = flipConfiguration.getFlip();
		assertThat(flips).isNotNull();
		assertThat(flips).containsEntry("greeting", false);
	}
	
    @Test
    void shouldUpdateConfigurationDynamically() {
        log.info("✅ Initial config: {}", flipConfiguration.getFlip());

        assertThat(flipConfiguration.getFlip().get("greeting")).isFalse();

        // Simule un changement externe (genre Cloud Config ou Vault)
        System.setProperty("r3edge.spring.flip.greeting", "true");

        log.info("🌀 Forcing refresh...");
        Set<String> refreshed = contextRefresher.refresh();
        log.info("🔁 Refreshed keys: {}", refreshed);

        // Check post-refresh value
        Boolean updatedValue = flipConfiguration.getFlip().get("greeting");
        log.info("✅ Updated value of 'greeting': {}", updatedValue);

        assertThat(updatedValue).isTrue();
    }
    
    @Test
    void testAllToggleCombinations() {
        // ⛔ A: false, B: false
        setFeature("featureA", false);
        setFeature("featureB", false);
        assertThat(service.featureAMethod()).isNull();
        assertThat(service.featureBMethod()).isNull();

        // ✅ A: true, B: false
        setFeature("featureA", true);
        setFeature("featureB", false);
        assertThat(service.featureAMethod()).isEqualTo("A OK");
        assertThat(service.featureBMethod()).isNull();

        // ✅ A: false, B: true
        setFeature("featureA", false);
        setFeature("featureB", true);
        assertThat(service.featureAMethod()).isNull();
        assertThat(service.featureBMethod()).isEqualTo("B OK");

        // ✅✅ A: true, B: true
        setFeature("featureA", true);
        setFeature("featureB", true);
        assertThat(service.featureAMethod()).isEqualTo("A OK");
        assertThat(service.featureBMethod()).isEqualTo("B OK");
    }
    
    @Test
    void shouldNotInjectFlippedBean() {
        assertThat(flippedBean).isNull(); // car greeting=false
    }

    @Test
    void shouldSkipFlipMethod() {
        String result = beanWithFlippedMethod.conditional();
        assertThat(result).isNull(); // intercepté par FlipMethodAspect
    }

    @Test
    void shouldRunNonFlippedMethod() {
        String result = beanWithFlippedMethod.unconditional();
        assertThat(result).isEqualTo("always-on");
    }
    
    @Test
    void shouldFlipAndUnflipMethodAtRuntime() {
        // Au démarrage : greeting = false
        log.info("Initial state: {}", flipConfiguration.getFlip());
        assertThat(flipConfiguration.getFlip().get("greeting")).isFalse();

        // Appel 1 → intercepté
        String firstCall = beanWithFlippedMethod.conditional();
        log.info("First call (disabled): {}", firstCall);
        assertThat(firstCall).isNull();

        // 🔁 Flip ON à chaud
        System.setProperty("r3edge.spring.flip.greeting", "true");
        contextRefresher.refresh();

        // Appel 2 → méthode activée
        String secondCall = beanWithFlippedMethod.conditional();
        log.info("Second call (enabled): {}", secondCall);
        assertThat(secondCall).isEqualTo("flip-method active");

        // 🔁 Flip OFF à chaud
        System.setProperty("r3edge.spring.flip.greeting", "false");
        contextRefresher.refresh();

        // Appel 3 → méthode désactivée à nouveau
        String thirdCall = beanWithFlippedMethod.conditional();
        log.info("Third call (disabled again): {}", thirdCall);
        assertThat(thirdCall).isNull();
    }
    
    private void setFeature(String key, boolean value) {
        System.setProperty("r3edge.spring.flip." + key, String.valueOf(value));
        contextRefresher.refresh();
    }

}
