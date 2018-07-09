package com.spring.example.controllers;

import com.spring.example.entity.AdminUser;
import com.spring.example.entity.User;
import com.spring.example.models.UserRegistration;
import com.spring.example.repository.AdminUserRepository;
import com.spring.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger("RegisterController");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @GetMapping("/admin/register/user")
    public String getRegisterPage() {
        return "register_user";
    }

    @PostMapping("/admin/register/user")
    public String registerUser(@RequestBody MultiValueMap paramMap, Model model, Principal principal) {

        UserRegistration data = new UserRegistration();
        data.setEmail(String.valueOf(paramMap.getFirst("email")));
        data.setName(String.valueOf(paramMap.getFirst("name")));
        data.setPhone(String.valueOf(paramMap.getFirst("phone")));
        data.setPassword(String.valueOf(paramMap.getFirst("password")));
        data.setPassword_confirm(String.valueOf(paramMap.getFirst("password_confirm")));

        if(data.validate()) {
            AdminUser adminUser = adminUserRepository.findByEmail(principal.getName());
            User user = new User(data.getEmail(),data.getPassword(),data.getPhone(),data.getName(), adminUser);
            userRepository.save(user);
        }
        else {
            return "redirect:/admin/register/user";
        }
        return "redirect:/admin/home";
    }


}
