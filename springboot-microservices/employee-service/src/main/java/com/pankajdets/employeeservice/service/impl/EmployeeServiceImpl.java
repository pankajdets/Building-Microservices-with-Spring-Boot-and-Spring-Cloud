package com.pankajdets.employeeservice.service.impl;

import org.springframework.stereotype.Service;

import com.pankajdets.employeeservice.dto.EmployeeDto;
import com.pankajdets.employeeservice.entity.Employee;
import com.pankajdets.employeeservice.repository.EmployeeRepository;
import com.pankajdets.employeeservice.service.EmployeeService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        
        //Convert EmployeeDto to Employee JPA Entity

        Employee employee = new Employee(
            employeeDto.getId(),
            employeeDto.getFirstName(),
            employeeDto.getLastName(),
            employeeDto.getEmail()
        );

        Employee savedEmployee = employeeRepository.save(employee);
        //Convert JPA Entity object to EmployeeDto

        EmployeeDto savedEmployeeDto = new EmployeeDto(
            savedEmployee.getId(),
            savedEmployee.getFirstName(),
            savedEmployee.getLastName(),
            savedEmployee.getEmail()
        );
        return savedEmployeeDto;
        
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        
       Employee employee =  employeeRepository.findById(employeeId).get();

       //Convert Employee JPA Entity to EmployeeDto

       EmployeeDto employeeDto = new EmployeeDto(
        employee.getId(),
        employee.getFirstName(),
        employee.getLastName(),
        employee.getEmail()
       );
       return employeeDto;
    }
    
}
