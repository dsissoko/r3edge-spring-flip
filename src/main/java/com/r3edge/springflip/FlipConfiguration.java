package com.r3edge.springflip;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "r3edge.spring")
public class FlipConfiguration {

    private Map<String, Boolean> flip;

    public Map<String, Boolean> getFlip() {
        return flip;
    }

    public void setFlip(Map<String, Boolean> flip) {
        this.flip = flip;
    }
}
