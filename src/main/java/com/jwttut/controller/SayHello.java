package com.jwttut.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SayHello {

    @RequestMapping("/sayHello")
    public String sayHello(){
        return "Hello! from private society";
    }

}
