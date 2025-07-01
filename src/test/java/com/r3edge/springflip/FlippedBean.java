package com.r3edge.springflip;

import org.springframework.stereotype.Component;

@FlipBean("greeting")
@Component
public class FlippedBean {
    public String ping() {
        return "should never be created";
    }
}
