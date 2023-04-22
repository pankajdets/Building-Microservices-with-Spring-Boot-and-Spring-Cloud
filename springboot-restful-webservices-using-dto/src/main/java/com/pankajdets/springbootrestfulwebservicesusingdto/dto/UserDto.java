package com.pankajdets.springbootrestfulwebservicesusingdto.dto;

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
    private String firstName;
    private String lastName;
    private String email;
}
