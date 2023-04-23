package com.pankajdets.springbootrestfulwebservicesusingdto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Don't include sensitive information in User DTO
//Beacuse We don't want to send sensitive information to client
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    
    //Requirement like User first name should not be null or empty
    @NotEmpty(message = "User first name should not be null or empty") 
    private String firstName;
    //Requirement like User last name should not be null or empty
    @NotEmpty(message = " User last name should not be null or empty")
    private String lastName;
    //Requirement like User email name should not be null or empty
    //Requirement like email address should be valid
    @NotEmpty(message = " User email name should not be null or empty")
    @Email(message = " email address should be valid")
    private String email;
}
