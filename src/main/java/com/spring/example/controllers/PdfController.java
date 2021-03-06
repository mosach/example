package com.spring.example.controllers;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.spring.example.configuration.DriveConfiguration;
import com.spring.example.entity.*;
import com.spring.example.models.DocumentLink;
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
import java.util.ArrayList;
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
    private UserRecordsRepository userRecordsRepository;

    @Autowired
    private UserFormRepository userFormRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping(value = "/user/preview/{form_number}", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> returnPdf(@PathVariable("form_number") Integer formNumber, Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        String html = buildHTMLForUserAndForm(formNumber, user);

        return getInputStreamResourceResponseEntity(formNumber, html);

    }

    @RequestMapping(value = "/user/download/{form_number}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadGoogleDoc(@PathVariable("form_number") Integer formNumber, Model model, Principal principal) throws IOException, GeneralSecurityException {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        UserRecords userRecords = userRecordsRepository.findFirstByUserIdAndFormIdOrderByIdDesc(user.getId(), Long.valueOf(formNumber));
        if (userRecords == null) {
            throw new ResourceNotFoundException();
        }
        String googleId = userRecords.getGoogleId();
        Drive drive = new DriveConfiguration().createDrive();
        InputStream inputStream = drive.files().export(googleId, "application/pdf").executeMedia().getContent();

        InputStreamResource resource = new InputStreamResource(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Q" + formNumber + ".pdf");

        String contentType = "application/octet-stream";
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @RequestMapping(value = "/admin/user/{user_id}/preview/{form_number}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> previewUserPdf(@PathVariable("user_id") Integer userId, @PathVariable("form_number") Integer formNumber, Model model, Principal principal) throws FileNotFoundException {
        String username = principal.getName();

        AdminUser adminUser = adminUserRepository.findByEmail(username);

        List<User> users = adminUser.getUser();
        User foundUser = null;
        boolean found = false;
        for (User user : users) {
            if (user.getId().equals(Long.valueOf(userId))) {
                found = true;
                foundUser = user;
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

        InputStreamResource resource = new InputStreamResource(new FileInputStream(new File(userRecords.getName())));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename="+foundUser.getEmail()+"_Q" + formNumber + ".doc");

        String contentType = "application/octet-stream";
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {

    }

    @GetMapping("/user/generate")
    public String generatePage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        model.addAttribute("user", user);
        List<DocumentLink> documentLinks = new ArrayList<>();
        Iterable<Documents> documents = documentRepository.findAll();

        for (Documents document : documents) {
            documentLinks.add(new DocumentLink(document.getId(),document.getName()));
        }

        model.addAttribute("document_links",documentLinks);
        return "generate";
    }

    @RequestMapping(value = "/user/generate/{form_number}", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> generateAndReturnPdf(@PathVariable("form_number") Integer formNumber, Model model, Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByEmail(username);

        String html = buildHTMLForUserAndForm(formNumber, user);


        String fileName = user.getId() + "_" + formNumber + "_" + Instant.now().toEpochMilli();
        String name = "/tmp/" + fileName + ".html";

        try

        {
            OutputStream outputStream1 = new FileOutputStream(name);
            PrintWriter printWriter = new PrintWriter(outputStream1);
            printWriter.print(html);
            printWriter.close();
            outputStream1.close();

            java.io.File filePath = new java.io.File(name);
            com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
            file.setName(user.getId()+"_"+formNumber);
            file.setMimeType("application/vnd.google-apps.document");

            OutputStream outputStream = new FileOutputStream("/tmp/" + fileName + ".doc");

            FileContent mediaContent = new FileContent("text/html", filePath);
            Drive drive = new DriveConfiguration().createDrive();
            com.google.api.services.drive.model.File uploaded = drive.files().create(file, mediaContent).setFields("id").execute();
            String id = uploaded.getId();


            UserRecords userRecords = new UserRecords();
            userRecords.setName("/tmp/" + fileName + ".doc");
            userRecords.setFormId(Long.valueOf(formNumber));
            userRecords.setUserId(user.getId());
            userRecords.setGoogleId(id);
            userRecordsRepository.save(userRecords);

            drive = new DriveConfiguration().createDrive();
            drive.files().export(id,"application/vnd.openxmlformats-officedocument.wordprocessingml.document").executeMediaAndDownloadTo(outputStream);
            outputStream.close();

        } catch (
                GeneralSecurityException | IOException e)

        {
            logger.error("Issue with Pdf processing",e);
            e.printStackTrace();
        }
        return getInputStreamResourceResponseEntity(formNumber, html);


    }

    private String buildHTMLForUserAndForm(@PathVariable("form_number") Integer formNumber, User user) {
        ClassLoaderTemplateResolver classLoaderTemplateResolver = buildTemplateResolver();
        return getHTML(formNumber, classLoaderTemplateResolver, user);
    }

    private ClassLoaderTemplateResolver buildTemplateResolver() {
        ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        classLoaderTemplateResolver.setSuffix(".html");
        classLoaderTemplateResolver.setTemplateMode("HTML5");
        classLoaderTemplateResolver.setCharacterEncoding("UTF-8");
        classLoaderTemplateResolver.setOrder(1);
        return classLoaderTemplateResolver;
    }

    private String getHTML( Integer formNumber, ClassLoaderTemplateResolver classLoaderTemplateResolver, User user) {
        List<UserForms> forms = userFormRepository.findByUserFormIdUserId(user.getId().intValue());

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(classLoaderTemplateResolver);

        Context context = new Context();
        Map<String, String> formMap;

        for (UserForms form : forms) {
            if(form != null ) {
                formMap = form.getMyMap();
                for (String s : formMap.keySet()) {
                    context.setVariable(s, formMap.get(s));
                }
            }
        }

        return templateEngine.process("templates/Q" + formNumber, context);
    }

    private ResponseEntity<InputStreamResource> getPdfInputStreamResourceResponseEntity(Integer formNumber, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Q" + formNumber + ".pdf");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }

    private ResponseEntity<InputStreamResource> getInputStreamResourceResponseEntity(Integer formNumber, String html) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Q" + formNumber + ".html");

        InputStream inputStream;
        inputStream = new ByteArrayInputStream(html.getBytes());
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.TEXT_HTML)
                .body(new InputStreamResource(inputStream));
    }


}
