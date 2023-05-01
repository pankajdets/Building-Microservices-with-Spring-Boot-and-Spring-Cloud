package com.pankajdets.employeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
            info = @Info(
                title = "Employee-Service REST API",
                description = "Employee-Service REST API Documentation",
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
                description = "Employee-Service Doc",
                url = "https://linkedin.com/in/pankajdets"
            )
)
@SpringBootApplication
@EnableFeignClients // Enables components scanning for interfaces that declare they are Feign clients
public class EmployeeServiceApplication {

	// @Bean
	// public RestTemplate restTemplate(){
	// 	return new RestTemplate();
	// }


	@Bean
	public  WebClient webClient(){
		return WebClient.builder().build();
	}

	

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

}
