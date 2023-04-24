package com.pankajdets.employeeservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pankajdets.employeeservice.dto.EmployeeDto;
import com.pankajdets.employeeservice.service.EmployeeService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/employees")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    //Buid Save Employee REST API
    @PostMapping
    public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto savedEmployeeDto = employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<EmployeeDto>(savedEmployeeDto, HttpStatus.CREATED);
    }
    
    //Build get Employee REST API

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") Long employeeId){
            EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
            return new ResponseEntity<EmployeeDto>(employeeDto, HttpStatus.OK);
    }
}
