package com.spring.example.controllers;

import com.spring.example.entity.*;
import com.spring.example.models.FormLink;
import com.spring.example.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger("ProfileController");

    @Resource
    private UserRepository userRepository;

    @Resource
    private AdminUserRepository adminUserRepository;

    @Autowired
    private FormEntityRepository formEntityRepository;

    @Autowired
    private Form2Repository form2Repository;

    @Autowired
    private Form3Repository form3Repository;

    @GetMapping("user/home")
    public String getLogin(Model model, Principal principal) {
        String emailAddress = principal.getName();
        model.addAttribute("email",emailAddress);
        User user = userRepository.findByEmail(emailAddress);
        model.addAttribute("name",user.getName());
        model.addAttribute("phone",user.getPhone());

        Form1 form1 = formEntityRepository.findByUserId(user.getId());
        Form2 form2 = form2Repository.findByUserId(user.getId());
        Form3 form3 = form3Repository.findByUserId(user.getId());

        List<FormLink> formLinkList = new ArrayList<>();

        if(form1 == null) {
            formLinkList.add(new FormLink(1,null,"Not Submitted","danger" ));
        }
        else {
            formLinkList.add(new FormLink(1,form1.getUpdatedAt(),"Submitted" ,"success"));
        }

        if(form2 == null) {
            formLinkList.add(new FormLink(2,null,"Not Submitted","danger" ));
        }
        else {
            formLinkList.add(new FormLink(2,form2.getUpdatedAt(),"Submitted","success" ));
        }

        if(form3 == null) {
            formLinkList.add(new FormLink(3,null,"Not Submitted","danger" ));
        }
        else {
            formLinkList.add(new FormLink(3,form3.getUpdatedAt(),"Submitted","success" ));
        }

        model.addAttribute("form_links",formLinkList);

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
    public String getAdminLogin(@RequestParam(required = false) String role, Model model, Principal principal) {

        String adminEmail = principal.getName();
        AdminUser adminUser = adminUserRepository.findByEmail(adminEmail);
        model.addAttribute("users",adminUser.getUser());

        return "admin_index";
    }

    @GetMapping("admin/profile")
    public String getAdminProfile(Model model, Principal principal) {

        String emailAddress = principal.getName();
        model.addAttribute("email",emailAddress);
        AdminUser user = adminUserRepository.findByEmail(emailAddress);
        model.addAttribute("name",user.getEmail());
        return "admin_profile";
    }

    @RequestMapping(value = "/admin/user/{user_id}", method = RequestMethod.GET)
    public String getUserProfile(@PathVariable("user_id") Integer userId, Model model, Principal principal) {
        String emailAddress = principal.getName();
        AdminUser adminUser = adminUserRepository.findByEmail(emailAddress);

        List<User> users =adminUser.getUser();
        boolean found = false;
        User foundUser = null;
        for (User user : users) {
            if(user.getId().equals(Long.valueOf(userId))) {
                found  = true;
                foundUser = user;
                break;
            }
        }
        if (!found) {
            throw new AccessDeniedException("User doesn't belong to your company");
        }
        model.addAttribute("email",emailAddress);
        AdminUser user = adminUserRepository.findByEmail(emailAddress);
        model.addAttribute("user",foundUser);
        model.addAttribute("count",7);
        return "admin_user_profile";
    }

}
