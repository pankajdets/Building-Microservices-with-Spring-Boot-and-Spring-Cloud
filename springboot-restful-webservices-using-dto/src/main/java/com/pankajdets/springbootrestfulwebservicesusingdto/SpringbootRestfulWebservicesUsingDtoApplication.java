package com.pankajdets.springbootrestfulwebservicesusingdto;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
	info =@Info(
		title = "Spring Boot REST API Documentation",
		description = "Spring Boot REST API Documentation",
		version ="v1.0.0",
		contact = @Contact(
			name = "Pankaj Ray",
			email ="pankajdets@gmail.com",
			url ="https://www.linkedin.com/in/pankaj-kumar-ray/"
		),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.linkedin.com/in/pankaj-kumar-ray/license"
		)
	),
	externalDocs =@ExternalDocumentation(
		description = "Spring Boot user Management Documentation",
		url = "https://www.linkedin.com/in/pankaj-kumar-ray/user-management.html"

	)
)
public class SpringbootRestfulWebservicesUsingDtoApplication {

	@Bean //Configure ModelMapper class as spring Bean and it will register in application context
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRestfulWebservicesUsingDtoApplication.class, args);
	}

}
