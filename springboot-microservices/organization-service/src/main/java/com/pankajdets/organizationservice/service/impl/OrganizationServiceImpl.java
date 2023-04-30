package com.pankajdets.organizationservice.service.impl;

import org.springframework.stereotype.Service;

import com.pankajdets.organizationservice.dto.OrganizationDto;
import com.pankajdets.organizationservice.entity.Organization;
import com.pankajdets.organizationservice.mapper.OrganizationMapper;
import com.pankajdets.organizationservice.repository.OrganizationRepository;
import com.pankajdets.organizationservice.service.OrganizationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationRepository organizationRepository;

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {
        //Conver OrganizationDto into Organization JPA Entity
        Organization organization = OrganizationMapper.mapToOrganization(organizationDto);
        Organization savedOrganization = organizationRepository.save(organization);

        // convert JPA Entity to Organization DTO and return
        return OrganizationMapper.mapToOrganizationDto(savedOrganization);
    }

    @Override
    public OrganizationDto getOrganizationByCode(String organizationCode) {
        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);

        //convert Organization JPA Entity to Organization DTO and return
        return OrganizationMapper.mapToOrganizationDto(organization);
    }
    
}
