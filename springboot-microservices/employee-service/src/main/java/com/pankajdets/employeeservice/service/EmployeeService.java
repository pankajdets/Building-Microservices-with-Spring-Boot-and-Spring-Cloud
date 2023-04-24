package com.pankajdets.employeeservice.service;

import com.pankajdets.employeeservice.dto.APIResponseDto;
import com.pankajdets.employeeservice.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    APIResponseDto getEmployeeById(Long employeeId);
    
}
