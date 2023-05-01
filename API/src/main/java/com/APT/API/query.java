package com.APT.API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class query {
    @GetMapping("/query")
    public String query(){
        return "Spring Boot Success";
    }
}
