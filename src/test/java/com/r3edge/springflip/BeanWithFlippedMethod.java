package com.r3edge.springflip;

import org.springframework.stereotype.Component;
@Component
public class BeanWithFlippedMethod {

    @FlipMethod("greeting")
    public String conditional() {
        return "flip-method active";
    }

    public String unconditional() {
        return "always-on";
    }
}
