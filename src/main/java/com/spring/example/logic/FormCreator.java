package com.spring.example.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.spring.example.entity.FormEntity;
import com.spring.example.entity.User;
import com.spring.example.pojo.Questionaire;
import com.spring.example.repository.FormEntityRepository;
import com.spring.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FormCreator {

    private static final Logger logger = LoggerFactory.getLogger("FormCreator");

    private String username;
    private final FormEntityRepository formEntityRepository;
    private final UserRepository userRepository;

    public FormCreator(String username, FormEntityRepository formEntityRepository, UserRepository userRepository) {
        this.username = username;
        this.formEntityRepository = formEntityRepository;
        this.userRepository = userRepository;
    }

    public int createOrLatest() {
        return 0;
    }

    public void addDefaultValues(Questionaire questionaire) {

    }

    public void saveAsEntity(Map<String, Object> data) {
        try {
            String json = new ObjectMapper().writeValueAsString(data);
            Map<String,String> map = new HashMap<>();
            addKeys("",new ObjectMapper().readTree(json),map);
            User user = userRepository.findByEmail(username);
            FormEntity formEntity = new FormEntity();
            formEntity.setUserId(user.getId());
            formEntity.setMyMap(map);
            formEntityRepository.save(formEntity);

//            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAService");
//            EntityManager em = emf.createEntityManager();
//            em.getTransaction().begin();
//            em.merge(formEntity);
//            em.flush();
//            em.getTransaction().commit();
//            em.close();

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
                addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }
}
