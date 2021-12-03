package com.bloodbank.bloodBankServer.config;


import java.util.List;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.FormEncoder;

public class FeignClientConfiguration {

    @Autowired
    protected ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Decoder feignDecoder() {
        HttpMessageConverter<?> jacksonConverter = new MappingJackson2HttpMessageConverter(decodeObjectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () ->
                new HttpMessageConverters(false, List.of(jacksonConverter));
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    @Bean
    public Encoder feignEncoder() {
        HttpMessageConverter<?> jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () ->
                new HttpMessageConverters(false, List.of(jacksonConverter));
        return new FormEncoder(new SpringEncoder(objectFactory));
    }

    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    public ObjectMapper decodeObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}