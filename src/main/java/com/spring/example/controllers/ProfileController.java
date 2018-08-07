package com.spring.example.controllers;

import com.spring.example.entity.*;
import com.spring.example.models.DocumentLink;
import com.spring.example.models.FormLink;
import com.spring.example.repository.*;
import jnr.ffi.annotations.In;
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
    private UserFormRepository userFormRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private UserRecordsRepository userRecordsRepository;

    @Autowired
    private DocumentRepository documentRepository;


    @GetMapping("user/home")
    public String getLogin(Model model, Principal principal) {
        String emailAddress = principal.getName();
        model.addAttribute("email",emailAddress);
        User user = userRepository.findByEmail(emailAddress);
        model.addAttribute("name",user.getName());
        model.addAttribute("phone",user.getPhone());

        List<FormLink> formLinkList = getFormLinks(user);

        model.addAttribute("form_links",formLinkList);

        return "index";
    }

    private List<FormLink> getFormLinks(User user) {
        List<UserForms> userForms = userFormRepository.findByUserFormIdUserId(user.getId().intValue());

        List<FormLink> formLinkList = new ArrayList<>();

        List<Integer> foundList = new ArrayList<>();

        for (UserForms userForm : userForms) {
            Integer formId = userForm.getUserFormId().getFormId();
            formLinkList.add(new FormLink(formId,userForm.getUpdatedAt(),"Submitted","success"));
            foundList.add(formId);
        }

        Iterable<Form> forms = formRepository.findAll();

        for (Form form : forms) {
            if(! foundList.contains(form.getId())) {
                formLinkList.add(new FormLink(form.getId(), null, "Not Submitted", "danger"));
            }
        }
        return formLinkList;
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
        model.addAttribute("user",foundUser);
        List<UserForms> userForms = userFormRepository.findByUserFormIdUserId(foundUser.getId().intValue());
        int filledForms = userForms.size();
        long totalForms = formRepository.count();

        double percentFilled = filledForms*100.0;
        percentFilled /= totalForms;


        List<DocumentLink> documentLinks = new ArrayList<>();
        Iterable<Documents> documents = documentRepository.findAll();

        List<UserRecords> distinctFormIdByUserId = userRecordsRepository.findDistinctFormIdByUserId(foundUser.getId());

        ArrayList<Integer> formList = new ArrayList<>();

        for (UserRecords userRecords : distinctFormIdByUserId) {
            formList.add(userRecords.getFormId().intValue());
        }

        int count = 0;
        for (Documents document : documents) {
            Integer id = document.getId();
            if(formList.contains(id)) {
                documentLinks.add(new DocumentLink(document.getId(),document.getName()));
            }
            count++;
        }

        double percent_docs = documentLinks.size()*100.0;
        percent_docs /= count;

        model.addAttribute("percent_fill",percentFilled);
        model.addAttribute("document_links",documentLinks);
        model.addAttribute("percent_docs",percent_docs);
        return "admin_user_profile";
    }

}
