package com.example.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register.html";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error.html";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home.html";
    }
}
