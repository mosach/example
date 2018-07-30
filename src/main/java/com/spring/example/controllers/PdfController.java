package com.spring.example.controllers;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.spring.example.configuration.DriveConfiguration;
import com.spring.example.entity.*;
import com.spring.example.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

//import org.w3c.tidy.Tidy;

@Controller
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger("PdfController");

    private static final String UTF_8 = "UTF-8";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private FormEntityRepository formEntityRepository;

    @Autowired
    private Form2Repository form2Repository;

    @Autowired
    private Form3Repository form3Repository;

    @Autowired
    private UserRecordsRepository userRecordsRepository;

    @RequestMapping(value = "/user/preview/{form_number}", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> returnPdf(@PathVariable("form_number") Integer formNumber, Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        UserRecords userRecords = userRecordsRepository.findFirstByUserIdAndFormIdOrderByIdDesc(user.getId(), Long.valueOf(formNumber));

        if (userRecords == null) {
            throw new ResourceNotFoundException();
        }

        String name = userRecords.getName();
        return getInputStreamResourceResponseEntity(formNumber, name);

    }

    @RequestMapping(value = "/admin/user/{user_id}/preview/{form_number}", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> previewUserPdf(@PathVariable("user_id") Integer userId, @PathVariable("form_number") Integer formNumber, Model model, Principal principal) throws FileNotFoundException {
        String username = principal.getName();

        AdminUser adminUser = adminUserRepository.findByEmail(username);

        List<User> users = adminUser.getUser();
        boolean found = false;
        for (User user : users) {
            if (user.getId().equals(Long.valueOf(userId))) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new AccessDeniedException("User doesn't belong to your company");
        }

        UserRecords userRecords = userRecordsRepository.findFirstByUserIdAndFormIdOrderByIdDesc(Long.valueOf(userId), Long.valueOf(formNumber));

        if (userRecords == null) {
            throw new ResourceNotFoundException();
        }

        String name = userRecords.getName();


        return getInputStreamResourceResponseEntity(formNumber, name);

    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {

    }

    @GetMapping("/user/generate")
    public String generatePage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        model.addAttribute("user", user);
        return "generate";
    }

    @RequestMapping(value = "/user/generate/{form_number}", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> generateAndReturnPdf(@PathVariable("form_number") Integer formNumber, Model model, Principal principal) {
        ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        classLoaderTemplateResolver.setSuffix(".html");
        classLoaderTemplateResolver.setTemplateMode("HTML5");
        classLoaderTemplateResolver.setCharacterEncoding("UTF-8");
        classLoaderTemplateResolver.setOrder(1);

        String username = principal.getName();
        User user = userRepository.findByEmail(username);

        Form1 form1 = formEntityRepository.findByUserId(user.getId());
        Form2 form2 = form2Repository.findByUserId(user.getId());
        Form3 form3 = form3Repository.findByUserId(user.getId());

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(classLoaderTemplateResolver);

        Context context = new Context();
        Map<String, String> formMap;
        if (form1 != null) {
            formMap = form1.getMyMap();
            for (String s : formMap.keySet()) {
                context.setVariable(s, formMap.get(s));
            }
        }

        if (form2 != null) {
            formMap = form2.getMyMap();
            for (String s : formMap.keySet()) {
                context.setVariable(s, formMap.get(s));
            }
        }
        if (form3 != null) {
            formMap = form3.getMyMap();
            for (String s : formMap.keySet()) {
                context.setVariable(s, formMap.get(s));
            }
        }


        String html = templateEngine.process("templates/Q" + formNumber, context);


        String fileName = user.getId() + "_" + formNumber + "_" + Instant.now().toEpochMilli();
        String name = "/tmp/" + fileName + ".html";

        try

        {
            OutputStream outputStream1 = new FileOutputStream(name);
            PrintWriter printWriter = new PrintWriter(outputStream1);
            printWriter.print(html);
            outputStream1.close();
            printWriter.close();
            UserRecords userRecords = new UserRecords();
            userRecords.setName(name);
            userRecords.setFormId(Long.valueOf(formNumber));
            userRecords.setUserId(user.getId());
            userRecordsRepository.save(userRecords);

            java.io.File filePath = new java.io.File(name);
            com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
            file.setName(user.getId()+"_"+formNumber);
            file.setMimeType("application/vnd.google-apps.document");

            OutputStream outputStream = new FileOutputStream("/tmp/"+fileName+".pdf");

            FileContent mediaContent = new FileContent("text/html", filePath);
            Drive drive = new DriveConfiguration().createDrive();
            com.google.api.services.drive.model.File uploaded = drive.files().create(file, mediaContent).setFields("id").execute();
            String id = uploaded.getId();
            drive = new DriveConfiguration().createDrive();
            Drive.Files.Export export = drive.files().export(id, "application/pdf");
            export.getMediaHttpDownloader().setDirectDownloadEnabled(true);
            export.executeMediaAndDownloadTo(outputStream);
            outputStream.close();

        } catch (
                GeneralSecurityException | IOException e)

        {
            logger.error("Issue with Pdf processing",e);
            e.printStackTrace();
        }
        return getInputStreamResourceResponseEntity(formNumber, name);


    }

    private ResponseEntity<InputStreamResource> getInputStreamResourceResponseEntity(@PathVariable("form_number") Integer formNumber, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Q" + formNumber + ".html");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.TEXT_HTML)
                .body(new InputStreamResource(inputStream));
    }


}
