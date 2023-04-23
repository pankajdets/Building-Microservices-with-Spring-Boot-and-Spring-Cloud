package com.pankajdets.springbootrestfulwebservicesusingdto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pankajdets.springbootrestfulwebservicesusingdto.dto.UserDto;
import com.pankajdets.springbootrestfulwebservicesusingdto.model.User;

@Mapper
public interface AutoUserMapper {
    //mapstruct will create implementation for these methods at compilation time
    //We don't have to implement these methods
    //In orderb to map one object to another object both obj should have same field names

    
    //It will provide Implementation of interfae AutoUserMapper at compilation time
    //We can use Mapper instance to call the mapping methods
    AutoUserMapper MAPPER = Mappers.getMapper(AutoUserMapper.class);    



    //Sometime fields name are fifferent in JPA Entity and DTO in that situation we can use
    //We can use @Mapping annotation having source=JPA Entity attribute name" and target= "DTO attribute name"
    //@Mapping(source="email", target= "emailAddress" )
    UserDto mapToUserDto(User user);
    User mapToUser(UserDto userDto);
}
