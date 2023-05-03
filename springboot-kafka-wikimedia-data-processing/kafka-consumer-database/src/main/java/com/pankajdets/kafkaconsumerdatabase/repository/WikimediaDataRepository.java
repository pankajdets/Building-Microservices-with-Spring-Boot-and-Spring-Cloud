package com.pankajdets.kafkaconsumerdatabase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankajdets.kafkaconsumerdatabase.entity.WikimediaData;

public interface WikimediaDataRepository extends JpaRepository<WikimediaData, Long> {
    
}
