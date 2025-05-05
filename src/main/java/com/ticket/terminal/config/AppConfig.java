/**
 * Конфигурационный класс для хранения параметров приложения, загружаемых из application.properties.
 * 
 * Содержит список разрешённых источников (allowedOrigins) для CORS.
 * Используется для централизованного управления настройками приложения.
 */
package com.ticket.terminal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class AppConfig {

    String[] allowedOrigins;
}
