package com.pankajdets.organizationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankajdets.organizationservice.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    //query method
    Organization findByOrganizationCode(String organizationCode);
}
