package com.spring.example.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.DocumentException;
import com.spring.example.entity.FormEntity;
import com.spring.example.entity.User;
import com.spring.example.repository.FormEntityRepository;
import com.spring.example.repository.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
//import org.w3c.tidy.Tidy;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;


import javax.print.Doc;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;

@Controller
public class PdfController {

    private static final String UTF_8 = "UTF-8";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormEntityRepository formEntityRepository;

    @RequestMapping(value = "/user/generate/{form_number}", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> generateAndReturnPdf(@PathVariable("form_number") Integer formNumber, Model model, Principal principal) {
        ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        classLoaderTemplateResolver.setSuffix(".html");
        classLoaderTemplateResolver.setTemplateMode("HTML5");
        classLoaderTemplateResolver.setCharacterEncoding("UTF-8");
        classLoaderTemplateResolver.setOrder(1);

        String username = principal.getName();
        User user = userRepository.findByEmail(username);

        FormEntity formEntity = formEntityRepository.findByUserId(user.getId());

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(classLoaderTemplateResolver);

        Context context = new Context();
        Map<String,String> formMap = formEntity.getMyMap();
        for (String s : formMap.keySet()) {
            context.setVariable(s,formMap.get(s));
        }

        try {
            PDDocument pdf = PDDocument.load(new File("/Users/mcherukuri/Downloads/Q26_Audit_Log.pdf"));
            Writer output = new PrintWriter("/Users/mcherukuri/Downloads/pdf.html", "utf-8");
            new PDFDomTree().writeText(pdf, output);

            output.close();
        }catch (ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }

        String html = templateEngine.process("templates/test2", context);

        org.jsoup.nodes.Document document1 = Jsoup.parse(html);
        document1.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

        try {
            String xhtml = document1.html();
            OutputStream outputStream = new FileOutputStream("/Users/mcherukuri/Downloads/form.pdf");
            ITextRenderer renderer = new ITextRenderer();
//            renderer.getFontResolver().addFont("static/ff1.ttf", IDENTITY_H, EMBEDDED);
//            renderer.getFontResolver().addFont("static/Georgia.ttf", IDENTITY_H, EMBEDDED);
//            renderer.getFontResolver().addFont("static/sans-serif.ttf", IDENTITY_H, EMBEDDED);
//            renderer.setDocumentFromString(html);
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
            outputStream.close();

        } catch ( IOException | DocumentException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=form.pdf");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/Users/mcherukuri/Downloads/form.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));



    }

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }

    private String prepareTempHTML(String clean_html) throws IOException {

        // XML 1.0 only allows the following characters
        // #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]

        String orig_html = null;
        Pattern pattern = null;
        Matcher matcher = null;
        pattern = Pattern.compile("[\\000]*");
        matcher = pattern.matcher(clean_html);
        if (matcher.find()) {
            orig_html = matcher.replaceAll("");
        }
        else {
            orig_html = clean_html;
        }

        return orig_html;
    }

}
