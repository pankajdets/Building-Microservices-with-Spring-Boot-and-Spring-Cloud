package com.pankajdets.employeeservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pankajdets.employeeservice.dto.APIResponseDto;
import com.pankajdets.employeeservice.dto.EmployeeDto;
import com.pankajdets.employeeservice.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(
    name = "Employee Service - EmployeeController",
    description = "Employee Controller Exposes REST API for Employee-Service"
)
@RestController
@RequestMapping("api/employees")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    //Buid Save Employee REST API
    @Operation(
        summary = "save Employee REST API",
        description = "save Employee REST API is used to save employee object in a database"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Http Status 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto savedEmployeeDto = employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<EmployeeDto>(savedEmployeeDto, HttpStatus.CREATED);
    }
    
    //Build get Employee REST API
    @Operation(
        summary = "get Employee REST API",
        description = "Get Employee REST API is used to get employee object from the database"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Http Status 200 SUCCESS"
    )
    @GetMapping("{id}")
    public ResponseEntity<APIResponseDto> getEmployee(@PathVariable("id") Long employeeId){
            APIResponseDto apiResponseDto = employeeService.getEmployeeById(employeeId);
            return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }
}
