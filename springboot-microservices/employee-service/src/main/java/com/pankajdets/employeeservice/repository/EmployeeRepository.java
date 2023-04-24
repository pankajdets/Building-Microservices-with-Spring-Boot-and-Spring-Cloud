package com.pankajdets.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pankajdets.employeeservice.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
}
