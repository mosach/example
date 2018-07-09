package com.spring.example.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger("LoginController");
    @GetMapping("/user/login")
    public String getLogin() {

        return "login";
    }

    @GetMapping("/admin/login")
    public String getAdminLogin() {
        return "login_admin";
    }

}
