package com.pankajdets.springbootrestfulwebservicesusingdto;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootRestfulWebservicesUsingDtoApplication {

	@Bean //Configure ModelMapper class as spring Bean and it will register in application context
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRestfulWebservicesUsingDtoApplication.class, args);
	}

}
