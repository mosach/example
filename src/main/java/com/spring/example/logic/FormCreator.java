package com.spring.example.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.spring.example.entity.*;
import com.spring.example.pojo.Element;
import com.spring.example.pojo.Page;
import com.spring.example.pojo.Questionaire;
import com.spring.example.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class FormCreator {

    private static final Logger logger = LoggerFactory.getLogger("FormCreator");

    private String username;
    private final UserRepository userRepository;

    public FormCreator(String username, UserRepository userRepository) {
        this.username = username;
        this.userRepository = userRepository;
    }

    public int createOrLatest() {
        return 0;
    }

    public void addDefaultValues(Questionaire questionaire, UserFormRepository userFormRepository) {
        User user = userRepository.findByEmail(username);
        List<UserForms> forms = userFormRepository.findByUserFormIdUserId(user.getId().intValue());

        Map<String, String> formMap;

        TreeMap<String, String> searchMap = new TreeMap<>();

        for (UserForms form : forms) {
            if(form != null ) {
                formMap = form.getMyMap();
                for (String s : formMap.keySet()) {
                    searchMap.put(s,formMap.get(s));
                }
            }
        }

        List<Page> pages = questionaire.getPages();

        for (Page page : pages) {
            List<Element> elements = page.getElements();
            for (Element element : elements) {
                String name = element.getDefaultValue();
                if(name != null && searchMap.containsKey(name)){
                    element.setDefaultValue(searchMap.get(name));
                }
            }
        }

    }

    public void saveAsEntity(FormEntityRepository formEntityRepository, Map<String, Object> data) {
        try {
            String json = new ObjectMapper().writeValueAsString(data);
            Map<String,String> map = new HashMap<>();
            addKeys("",new ObjectMapper().readTree(json),map);
            User user = userRepository.findByEmail(username);
            Form1 form1 = new Form1();
            form1.setUserId(user.getId());
            form1.setMyMap(map);
            formEntityRepository.save(form1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsEntity(Form2Repository formEntityRepository, Map<String, Object> data) {
        try {
            String json = new ObjectMapper().writeValueAsString(data);
            Map<String,String> map = new HashMap<>();
            addKeys("",new ObjectMapper().readTree(json),map);
            User user = userRepository.findByEmail(username);
            Form2 form2 = new Form2();
            form2.setUserId(user.getId());
            form2.setMyMap(map);
            formEntityRepository.save(form2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsEntity(Form3Repository formEntityRepository, Map<String, Object> data) {
        try {
            String json = new ObjectMapper().writeValueAsString(data);
            Map<String,String> map = new HashMap<>();
            addKeys("",new ObjectMapper().readTree(json),map);
            User user = userRepository.findByEmail(username);
            Form3 form3 = new Form3();
            form3.setUserId(user.getId());
            form3.setMyMap(map);
            formEntityRepository.save(form3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsEntity(Form7Repository formEntityRepository, Map<String, Object> data) {
        try {
            String json = new ObjectMapper().writeValueAsString(data);
            Map<String,String> map = new HashMap<>();
            addKeys("",new ObjectMapper().readTree(json),map);
            User user = userRepository.findByEmail(username);
            Form7 form3 = new Form7();
            form3.setUserId(user.getId());
            form3.setMyMap(map);
            formEntityRepository.save(form3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath;

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                addKeys(currentPath + "_" + i + "_", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }

    public void saveAsEntity(UserFormRepository userFormRepository, Map<String, Object> data, Integer formNumber) {


        try {
            String json = new ObjectMapper().writeValueAsString(data);
            Map<String,String> map = new HashMap<>();
            addKeys("",new ObjectMapper().readTree(json),map);
            User user = userRepository.findByEmail(username);
            UserForms form = new UserForms();
            UserFormId userFormId = new UserFormId(user.getId().intValue(),formNumber);
            form.setUserFormId(userFormId);
            form.setMyMap(map);
            userFormRepository.save(form);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
