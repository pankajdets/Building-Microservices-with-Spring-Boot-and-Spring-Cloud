package com.pankajdets.departmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
@OpenAPIDefinition(
	info = @Info(
		title = "Department-Service REST API",
		description = "Department-Service REST API Documentation",
		version = "v1.0",
		contact = @Contact(
			name = "Pankaj",
			email = "pankajdets@gmail.com",
			url = "https://linkedin.com/in/pankajdets"
		),
		license = @License(
			name = "Apache 2.0",
			url = "https://linkedin.com/in/pankajdets"
		)
	),
	externalDocs = @ExternalDocumentation(
		description = "Department-Service Doc",
		url = "https://linkedin.com/in/pankajdets"
	)
)
@SpringBootApplication
public class DepartmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepartmentServiceApplication.class, args);
	}

}
