package com.r3edge.springflip;

import org.springframework.stereotype.Component;

@Component
public class MultiFeatureService {

    @FlipMethod("featureA")
    public String featureAMethod() {
        return "A OK";
    }

    @FlipMethod("featureB")
    public String featureBMethod() {
        return "B OK";
    }
}

