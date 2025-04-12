package com.ticket.terminal;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SecurityScheme(
		name = "basicAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "basic"
)
@OpenAPIDefinition(
		security = {@SecurityRequirement(name = "basicAuth")}
)
@SpringBootApplication
public class TicketTerminalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketTerminalApiApplication.class, args);
	}

}
