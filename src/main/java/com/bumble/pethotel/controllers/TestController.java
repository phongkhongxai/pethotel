package com.bumble.pethotel.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/no-auth")
public class TestController {

    @GetMapping
    public String helloWorld(){
        return "Hello World";
    }
}
