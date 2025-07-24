/**
 * Конфигурация Feign-клиентов для интеграции с внешними REST API.
 * 
 * Определяет уровень логирования, настройки сериализации/десериализации JSON, а также
 * добавляет необходимые HTTP-заголовки для запросов.
 * Используется всеми Feign-клиентами в проекте (например, MuseumClient).
 */
package com.ticket.terminal.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignClientConfiguration {

    private final MappingJackson2HttpMessageConverter jacksonConverter;
    
    @Value("${application.clients.tonline-gate.login}")
    private String login;
    
    @Value("${application.clients.tonline-gate.password}")
    private String password;

    public FeignClientConfiguration(MappingJackson2HttpMessageConverter jacksonConverter) {
        this.jacksonConverter = jacksonConverter;
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor tonlineInterceptor() {
        return template -> {
            template.header("Accept", "application.yml/json,text/json;charset=UTF-8,text/plain");
            template.header("Content-Type", "application.yml/json;charset=UTF-8");
            template.header("Accept-Encoding", "gzip");
        };
    }
    
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(login, password);
    }

    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(() -> new HttpMessageConverters(jacksonConverter));
    }

    @Bean
    public Decoder feignDecoder() {
        return new SpringDecoder(() -> new HttpMessageConverters(jacksonConverter));
    }
}



