/**
 * Конфигурация Jackson для сериализации и десериализации JSON в приложении.
 * 
 * Определяет:
 * - Форматы дат и времени (LocalDate, LocalDateTime, ZonedDateTime)
 * - Кастомные десериализаторы (например, CustomZonedDateTimeDeserializer)
 * - Поддерживаемые типы контента для HTTP (application/json, text/json и др.)
 * 
 * Используется для корректной работы с датами и форматами данных во всех REST-контроллерах и клиентах.
 */
package com.ticket.terminal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(ZonedDateTime.class, new CustomZonedDateTimeDeserializer());

        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        timeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(dateTimeFormatter));

        mapper.registerModule(timeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

        return mapper;
    }


    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);

        List<MediaType> types = new ArrayList<>(converter.getSupportedMediaTypes());
        types.add(MediaType.APPLICATION_JSON);
        types.add(MediaType.valueOf("text/json"));
        types.add(MediaType.valueOf("text/json;charset=UTF-8"));
        types.add(MediaType.TEXT_PLAIN);
        types.add(MediaType.valueOf("text/plain;charset=UTF-8"));
        converter.setSupportedMediaTypes(types);

        return converter;
    }
}
