package com.r3edge.springflip;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest(classes = TestApplication.class)
@ExtendWith(OutputCaptureExtension.class)
public class SpringFlipIntegrationTest {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Test
	void shouldLoadTasksFromYaml() {
		// TODO : récupérer la liste des features à partir du yaml
	}

}
