package com.spring.example.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.example.logic.FormCreator;
import com.spring.example.pojo.Questionaire;
import com.spring.example.repository.FormEntityRepository;
import com.spring.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Stream;

@Controller
public class GdprController {

    private static final Logger logger = LoggerFactory.getLogger("GdprController");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormEntityRepository formEntityRepository;

    @GetMapping("/form/create")
    public String createNewGdprForm(Model model, Principal principal) {
        return "";
    }

    @GetMapping("/form/{form_number}")
    public String getGdprForm(@PathVariable("form_number") Integer formNumber, Model model, Principal principal) {
        StringBuilder contentBuilder = new StringBuilder();

        String path = getClass().getClassLoader().getResource("static/section1.json").getPath();

        String username = principal.getName();

        try (Stream<String> stream = Files.lines( Paths.get(path), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        FormCreator formCreator = new FormCreator(username, formEntityRepository, userRepository);
        String json =  contentBuilder.toString();
        model.addAttribute("survey",json);
        model.addAttribute("form_number",1);
        try {
            Questionaire questionaire = new ObjectMapper().readValue(new File(path), Questionaire.class);
            formCreator.addDefaultValues(questionaire);
            String survey = new ObjectMapper().writeValueAsString(questionaire);
            model.addAttribute("survey",survey);
        } catch (IOException e) {
            e.printStackTrace();                                    // No need to fail if the default values cannot be loaded
        }
        return "gdpr_form";
    }

    @PostMapping("/user/form/{form_number}")
    public String postGDPRForm(@PathVariable("form_number") Integer formNumber, @RequestBody Map<String,Object> data, Model model, Principal principal) {

        String username = principal.getName();
        FormCreator formCreator = new FormCreator(username,formEntityRepository,userRepository);
        formCreator.saveAsEntity(data);
        logger.info("Data received"+ data);
        return "redirect:/user/home";
    }

}
