package com.pankajdets.employeeservice.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.pankajdets.employeeservice.dto.APIResponseDto;
import com.pankajdets.employeeservice.dto.DepartmentDto;
import com.pankajdets.employeeservice.dto.EmployeeDto;
import com.pankajdets.employeeservice.entity.Employee;
import com.pankajdets.employeeservice.repository.EmployeeRepository;
import com.pankajdets.employeeservice.service.EmployeeService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    //private RestTemplate restTemplate; //Constructor based dependency injection
    private WebClient webClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        
        //Convert EmployeeDto to Employee JPA Entity

        Employee employee = new Employee(
            employeeDto.getId(),
            employeeDto.getFirstName(),
            employeeDto.getLastName(),
            employeeDto.getEmail(),
            employeeDto.getDepartmentCode()
        );

        Employee savedEmployee = employeeRepository.save(employee);
        //Convert JPA Entity object to EmployeeDto

        EmployeeDto savedEmployeeDto = new EmployeeDto(
            savedEmployee.getId(),
            savedEmployee.getFirstName(),
            savedEmployee.getLastName(),
            savedEmployee.getEmail(),
            savedEmployee.getDepartmentCode()
        );
        return savedEmployeeDto;
        
    }

    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {
        
       Employee employee =  employeeRepository.findById(employeeId).get();

       //ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/"+employee.getDepartmentCode(), DepartmentDto.class);
        //DepartmentDto departmentDto = responseEntity.getBody();

        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/"+employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

       //Convert Employee JPA Entity to EmployeeDto

       EmployeeDto employeeDto = new EmployeeDto(
        employee.getId(),
        employee.getFirstName(),
        employee.getLastName(),
        employee.getEmail(),
        employee.getDepartmentCode()
       );
        
       
       APIResponseDto apiResponseDto = new APIResponseDto(employeeDto, departmentDto);
       return apiResponseDto;
    }
    
}
