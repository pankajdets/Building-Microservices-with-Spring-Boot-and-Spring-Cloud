package com.pankajdets.springootrestfulwebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankajdets.springootrestfulwebservices.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
