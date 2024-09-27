package com.jdragon.cqhttp.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class ObjectMapperHolder {
    public static final ObjectMapper SNAKE_CASE_MAPPER;
    public static final ObjectMapper MAPPER;

    static {
        SNAKE_CASE_MAPPER = new ObjectMapper();
        SNAKE_CASE_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        SNAKE_CASE_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MAPPER = new ObjectMapper();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
