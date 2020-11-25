package com.aconcaguasf.basa.digitalize.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;

public enum Commons {
    INSTANCE;

    private final ObjectMapper commonMapper;
    private final Gson commonGson;

    Commons() {
        commonMapper = new ObjectMapper();
        commonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        commonMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        commonGson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
    }

    public ObjectMapper getCommonMapper() {
        return commonMapper;
    }

    public Gson getCommonGson() {
        return commonGson;
    }

}