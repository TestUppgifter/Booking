package com.example.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "forward:login.html";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "forward:register.html";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "forward:error.html";
    }
}
