package com.pankajdets.departmentservice.service.impl;

import org.springframework.stereotype.Service;

import com.pankajdets.departmentservice.dto.DepartmentDto;
import com.pankajdets.departmentservice.entity.Department;
import com.pankajdets.departmentservice.repository.DepartmentRepository;
import com.pankajdets.departmentservice.service.DepartmentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

    private DepartmentRepository departmentRepository; //Constructor based dependency injection



    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
        //Convert DepartmentDto object to Department JPA Entity
        Department department = new Department(
            departmentDto.getId(),
            departmentDto.getDepartmentName(),
            departmentDto.getDepartmentDescription(),
            departmentDto.getDepartmentCode()

        );

       Department savedDepartment = departmentRepository.save(department);

       //Convert Department object savedDepartment to Department Dto

       DepartmentDto savedDepartmentDto = new DepartmentDto(
        savedDepartment.getId(),
        savedDepartment.getDepartmentName(),
        savedDepartment.getDepartmentDescription(),
        savedDepartment.getDepartmentCode()
       );

       return savedDepartmentDto;
       
    }



    @Override
    public DepartmentDto getDepartmentByCode(String departmentCode) {
       Department department = departmentRepository.findByDepartmentCode(departmentCode);
       DepartmentDto departmentDto = new DepartmentDto(
        department.getId(),
        department.getDepartmentName(),
        department.getDepartmentDescription(),
        department.getDepartmentCode()    
        );
        return departmentDto;
    }
    
}
