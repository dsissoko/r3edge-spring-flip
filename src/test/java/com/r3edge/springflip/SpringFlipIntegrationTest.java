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
}
