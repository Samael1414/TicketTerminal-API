package com.ticket.terminal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class FeignClientConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor tonlineInterceptor() {
        return template -> {
            // Стереть возможные заголовки, проставленные SpringEncoder-ом
            template.headers().remove("Content-Type");
            template.headers().remove("Accept");

            // Поставить правильные
            template.header("Content-Type", "application/json;charset=utf-8");
            template.header("Accept",       "application/json;charset=utf-8");
            template.header("Accept-Encoding", "gzip");   // шлюз умеет отдавать gzip
        };
    }

    @Bean
    public Encoder feignEncoder(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

        return new SpringEncoder(() -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(mapper)
        ));
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
        return new SpringDecoder(() -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(mapper)
        ));
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        mediaTypes.add(MediaType.valueOf("text/json"));
        mediaTypes.add(MediaType.valueOf("text/json;charset=UTF-8"));
        converter.setSupportedMediaTypes(mediaTypes);
        System.out.println("[REGISTERED] customJackson2HttpMessageConverter with text/json support");
        return converter;
    }

}
