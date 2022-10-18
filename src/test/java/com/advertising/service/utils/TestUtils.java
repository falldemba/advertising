package com.advertising.service.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class TestUtils {
    public final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private ObjectMapper mapper;

    public byte[] convertObjectToJsonBytes(Object object) throws IOException{
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public <O extends Object> List<O> convertJsonToObjectList(String json, Class<O> className) throws IOException {
        CollectionLikeType type = mapper.getTypeFactory().constructCollectionLikeType(List.class, className);
        return mapper.readValue(json, type);
    }
}
