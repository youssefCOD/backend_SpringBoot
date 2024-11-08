package com.MyBackEnd.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String getHome(){
        return "home page";
    }

    @GetMapping("/test")
    public String getTest(){
        return "hello test";
    }

    @GetMapping("/app/admin")
    public String getAdmin(){
        return "Admin page";
    }

    @GetMapping("/app/dashboard")
    public String getDashboard(){
        return "this is the Dashboard";
    }
}
