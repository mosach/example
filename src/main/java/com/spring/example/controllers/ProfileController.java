package com.spring.example.controllers;

import com.spring.example.entity.User;
import com.spring.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.Principal;

@Controller
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger("ProfileController");

    @Resource
    private UserRepository userRepository;

    @GetMapping("user/home")
    public String getLogin(Model model) {

        return "index";
    }

    @GetMapping("user/profile")
    public String getProfile(Model model, Principal principal) {

        String emailAddress = principal.getName();
        model.addAttribute("email",emailAddress);
        User user = userRepository.findByEmail(emailAddress);
        model.addAttribute("name",user.getName());
        model.addAttribute("phone",user.getPhone());
        return "profile";
    }


    @GetMapping("/admin/home")
    public String getAdminLogin(@RequestParam(required = false) String role, Model model) {

        return "index";
    }

}
