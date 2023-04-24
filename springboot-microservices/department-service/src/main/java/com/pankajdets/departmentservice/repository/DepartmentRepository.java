package com.pankajdets.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankajdets.departmentservice.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDepartmentCode(String depaermentCode);
}
