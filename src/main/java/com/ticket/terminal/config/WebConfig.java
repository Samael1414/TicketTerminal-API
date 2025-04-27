package com.ticket.terminal.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jsonConverter) {
                jsonConverter.getObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                List<MediaType> mediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
                mediaTypes.add(MediaType.valueOf("text/json"));
                mediaTypes.add(MediaType.valueOf("text/json;charset=UTF-8"));
                jsonConverter.setSupportedMediaTypes(mediaTypes);
                System.out.println("[INFO] Registered support for text/json and text/json;charset=UTF-8");
            }
        }
    }

}
