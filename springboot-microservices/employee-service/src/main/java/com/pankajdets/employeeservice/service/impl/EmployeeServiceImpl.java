package com.pankajdets.employeeservice.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.pankajdets.employeeservice.dto.APIResponseDto;
import com.pankajdets.employeeservice.dto.DepartmentDto;
import com.pankajdets.employeeservice.dto.EmployeeDto;
import com.pankajdets.employeeservice.dto.OrganizationDto;
import com.pankajdets.employeeservice.entity.Employee;
import com.pankajdets.employeeservice.mapper.EmployeeMapper;
import com.pankajdets.employeeservice.repository.EmployeeRepository;
import com.pankajdets.employeeservice.service.APIClient;
import com.pankajdets.employeeservice.service.EmployeeService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeServiceImpl.class);
    private EmployeeRepository employeeRepository;
    //private RestTemplate restTemplate; //Constructor based dependency injection
    private WebClient webClient;

    private APIClient apiClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        
        //Convert EmployeeDto to Employee JPA Entity

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        Employee savedEmployee = employeeRepository.save(employee);
        //Convert JPA Entity object to EmployeeDto

        EmployeeDto savedEmployeeDto = EmployeeMapper.mapToEmployeeDto(savedEmployee);
        return savedEmployeeDto;
        
    }

    //@CircuitBreaker(name ="${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name ="${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {
     
        LOGGER.info("inside getEmployeeById() method");
        
       Employee employee =  employeeRepository.findById(employeeId).get();

       //Below two line of code is for RestTemplate communication
       //ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/"+employee.getDepartmentCode(), DepartmentDto.class);
        //DepartmentDto departmentDto = responseEntity.getBody();

        //Below  code is for WebClient communication
        // DepartmentDto departmentDto = webClient.get()
        //         .uri("http://localhost:8080/api/departments/"+employee.getDepartmentCode())
        //         .retrieve()
        //         .bodyToMono(DepartmentDto.class)
        //         .block();

        // Below  code is for Open Feign communication (Calling getDepartment REST API )
        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());

        // Below  code is for WebClient communication(Calling getOrganization REST API )
        OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8083/api/organizations/"+employee.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();


       //Convert Employee JPA Entity to EmployeeDto

       EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(employee);
        
       
       APIResponseDto apiResponseDto = new APIResponseDto(employeeDto, departmentDto, organizationDto);
       return apiResponseDto;
    }
    
    //Implement fallback method i.e getDefaultDepartment

    public APIResponseDto getDefaultDepartment(Long employeeId, Exception exception){
        LOGGER.info("inside getDefaultDepartment() method");

        Employee employee = employeeRepository.findById(employeeId).get();

       DepartmentDto departmentDto = new DepartmentDto();
       //set default value to object departmentDto
       departmentDto.setDepartmentName("R&D Department");
       departmentDto.setDepartmentCode("RD001");
       departmentDto.setDepartmentDescription("Research and Development Department");

       //Convert Employee JPA Entity to EmployeeDto

       EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(employee);
        
       
       APIResponseDto apiResponseDto = new APIResponseDto();
       apiResponseDto.setEmployee(employeeDto);
       apiResponseDto.setDepartment(departmentDto);
       return apiResponseDto;

    }
}
