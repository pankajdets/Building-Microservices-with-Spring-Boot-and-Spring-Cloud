package com.pankajdets.springbootrestfulwebservicesusingdto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankajdets.springbootrestfulwebservicesusingdto.model.User;



public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
}