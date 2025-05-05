package com.ticket.terminal;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SecurityScheme(
		name = "basicAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "basic"
)
@OpenAPIDefinition(
		security = {@SecurityRequirement(name = "basicAuth")}
)
@SpringBootApplication(scanBasePackages = "com.ticket.terminal")
@EnableJpaAuditing
@EnableFeignClients(basePackages = "com.ticket.terminal")
public class TicketTerminalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketTerminalApiApplication.class, args);
	}

}
