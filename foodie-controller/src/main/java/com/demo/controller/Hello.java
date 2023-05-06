package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @GetMapping("/v1/hello")
    public String hello(){
        System.out.println("hello enter");
        return "hello";
    }
}
