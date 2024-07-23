package com.bootagit_project_1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {


    @GetMapping("/")
    public String main(){ return "Hello, world!"; }

    @GetMapping("/api/data")
    public String test(){ return "Hello, API!"; }

}

