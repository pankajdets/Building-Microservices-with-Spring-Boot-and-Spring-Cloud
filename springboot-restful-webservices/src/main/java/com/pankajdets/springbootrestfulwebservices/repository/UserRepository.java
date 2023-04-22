package com.pankajdets.springbootrestfulwebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankajdets.springbootrestfulwebservices.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
