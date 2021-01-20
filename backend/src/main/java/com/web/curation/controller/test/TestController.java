package com.web.curation.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String hello(){
        return "hello";
    }

    @GetMapping("/all")
    public String all(){
        return "all user can access this controller";
    }
}
