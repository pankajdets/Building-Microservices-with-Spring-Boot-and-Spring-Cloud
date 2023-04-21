package com.pankajdets.springbootrestapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pankajdets.springbootrestapi.model.Student;



@RestController
public class StudentController {

    @GetMapping("student")
    public Student getStudent(){

        Student student = new Student(1, "Pankaj", "Ray");
        return student;
        
    }
    

    @GetMapping("students")
    public List<Student> getStudents(){
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Pankaj", "Ray"));
        students.add(new Student(1, "Raju", "Ray"));
        students.add(new Student(1, "Bablu", "Ray"));
        
        return students;
        
    }


    //Spring Boot Rest API with Path Variable
    //{id} - URI template Variable
    //http://localhost:8080/students/1
    //@PathVariable annotation used on a method to bind it to the value of a URI template variable
    // Whenever the "URI template Variable"  and method argument have same name then we don't have to give argument for @Pathvariable
    //Otherwise
    //@GetMapping("student/{id}")
    //public Student studentPathVariable(@PathVariable("id") int studentId)

    @GetMapping("student/{id}")
    public Student studentPathVariable(@PathVariable int id){
        return new Student(id,"Pankaj", "Ray");
    }


    //Rest Api to handle multiple path variable in Request Url
    //http://localhost:8080/students/1/Pankaj/Ray
    @GetMapping("student/{id}/{first-name}/{last-name}")
    public Student studentPathVariable(@PathVariable("id") int Studentid, @PathVariable("first-name") String firstName, @PathVariable("last-name") String lastName ){
        return new Student(Studentid, firstName, lastName);
    }

    //Spring Boot Rest API with Query Param
    //http://localhost:8080/student/query?id=1
    @GetMapping("/student/query")
    public Student studentRequestVariable(@RequestParam int id){
        return new Student(id,"Pankaj", "Ray");
    }

    //Multiple Query Parameter  in URL
    //http://localhost:8080/student/multiplequery?id=1&firstName=Pankaj&lastName=Ray
    @GetMapping("/student/multiplequery")
    public Student studentRequestVariable(@RequestParam int id,@RequestParam String firstName, @RequestParam String lastName){
        return new Student(id,firstName, lastName);
    }

    //Spring Boot Rest API that handles HTTP POST request
    //@PostMapping and @RequestBody
    //@PostMapping annotation is used for mapping HTTP POST request onto specific handler method
    //@RequestBody annotation responsible to retrieving HTTP request body and automatically converting into java object
    //It internally uses Spring provided HttpMessageConverter to convert json into java object
    //If our Rest API don't return any http response then by default spring boot will send http status as 200 (ok). But here we have created new resource  hence this api should return http response 201 (created)
    @PostMapping("student/create")
    @ResponseStatus(HttpStatus.CREATED) //To send http status code in response
    public Student createStudent(@RequestBody Student student){
        System.out.println(student.getId());
        System.out.println(student.getFirstName());
        System.out.println(student.getLastName());
        return student;
    }


    //Spring boot Rest API that handles HTTP PUT Request - updating existing resource
    //@PutMapping annotation for mapping HTTP PUT request onto specific handler method
    // PUTMapping should return response status 200 (ok) and by default spring boot is returing 200(ok) hence we are not returing status code manually
    @PutMapping("students/{id}/update")
    public  Student updateStudent(@RequestBody Student student, @PathVariable("id") int studentId){
        System.out.println(student.getFirstName());
        System.out.println(student.getLastName());
        student.setId(studentId);
        return student;
    }

    //Spring Boot REST API that handles HTTP DELETE Request- deleting the existing resource
    // DeleteMapping should return response status 200 (ok) and by default spring boot is returing 200(ok) hence we are not returing status code manually
    @DeleteMapping("students/{id}/delete")
    public String deleteStudent(@PathVariable("id") int studentId){
        System.out.println(studentId);
        return "student deleted successfully";
    }

}
