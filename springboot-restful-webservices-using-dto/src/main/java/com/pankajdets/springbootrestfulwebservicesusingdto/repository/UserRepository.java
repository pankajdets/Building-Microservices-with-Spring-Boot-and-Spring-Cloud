package com.pankajdets.springbootrestfulwebservicesusingdto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankajdets.springbootrestfulwebservicesusingdto.model.User;



public interface UserRepository extends JpaRepository<User, Long> {
    
}