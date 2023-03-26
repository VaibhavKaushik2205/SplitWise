package com.project.splitwise.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JacksonUtils {

    private final ObjectMapper objectMapper;

    public String objectToString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("object to string conversion failed", e);
        }
    }

    public <T> T stringToObject(String s, Class<T> klazz) {
        try {
            return objectMapper.readValue(s, klazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("string to object conversion failed", e);
        }
    }

    public <T> T treeToValue(TreeNode n, Class<T> valueType) {
        try {
            return objectMapper.treeToValue(n, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json node to object conversion failed", e);
        }
    }

    public <T> List<T> stringToListObject(String s, Class<T> klazz) {
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, klazz);

            return objectMapper.readValue(s, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("string to list object conversion failed", e);
        }
    }

    public <T> T convertValue(Object fromClass, Class<T> klazz) {
        try {
            return objectMapper.convertValue(fromClass, klazz);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("class conversion failed", e);
        }
    }
}

