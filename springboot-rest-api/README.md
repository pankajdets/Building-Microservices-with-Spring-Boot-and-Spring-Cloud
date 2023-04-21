**Spring Boot REST API**


**Basic Concept of Spring REST**


![Alt text](https://user-images.githubusercontent.com/42623098/233729699-07672430-e413-46ae-9a01-77e16a60fda3.png)


 

Generate project from Spring Initializer and having dependency "**Spring Web**"
**Spring Web Dependency:**  Provide Embedded Tomcat Container
Whenever we develop Restful web services using spring mvc all Rest API return JSON to client.
**@Controller** : To make class MVC Controller
**@ResponseBody** : To convert java object into json

**@Controller** // To make class MVC Controller
**@ResponseBody** // It tells a controller that object returned is automatically serialized into JSON and passed back into HttpResponse object
//This annotation will convert java object to json
Instead of using these two controller we can use single controller @RestController
**@RestController = @Controller + @ResponseBody **


**1.Get Request**
**PathVariable**

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


**Request Param**
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

**2. Post Request: Create new Resource**

If our Rest API don't return any http response then by default spring boot will send http status as 200 (ok). But here we have created new resource  hence this api should return http response 201 (created)

//Spring Boot Rest API that handles HTTP POST request
//@PostMapping and @RequestBody
//@PostMapping annotation is used for mapping HTTP POST request onto specific handler method
 //@RequestBody annotation responsible to retrieving HTTP request body and automatically converting into java object
 //It internally uses Spring provided HttpMessageConverter to convert json into java object
//If our Rest API don't return any http response then by default spring boot will send http status as 200 (ok). But here we have created new resource  hence this api should return http response 201 (created)
//http://localhost:8080/student/create
    @PostMapping("student/create")
    @ResponseStatus(HttpStatus.CREATED) //To send http status code in response
    public Student createStudent(@RequestBody Student student){
        System.out.println(student.getId());
        System.out.println(student.getFirstName());
        System.out.println(student.getLastName());
        return student;
    }

**3. Put Request: To update existing Resource**

//Spring boot Rest API that handles HTTP PUT Request - updating existing resource
//@PutMapping annotation for mapping HTTP PUT request onto specific handler method
// PUTMapping should return response status 200 (ok) and by default spring boot is returing 200(ok) hence we are not returing status code manually
//http://localhost:8080/students/2/update
    @PutMapping("students/{id}/update")
    public  Student updateStudent(@RequestBody Student student,@PathVariable("id") int studentId){
        System.out.println(student.getFirstName());
        System.out.println(student.getLastName());
        student.setId(studentId);
        return student;
    }

**4. Delete Request: To delete existing Resource**


//Spring Boot REST API that handles HTTP DELETE Request- deleting the existing resource
// DeleteMapping should return response status 200 (ok) and by default spring boot is returing 200(ok) hence we are not returing status code manually
    @DeleteMapping("students/{id}/delete")
    public String deleteStudent(@PathVariable("id") int studentId){
        System.out.println(studentId);
        return "student deleted successfully";
    }

**##Using Spring ResponseEntity to manipulate the HTTP Response**
⦁	ResponseEntity represents the whole HTTP response: status code,headers, and body. As a result, we can use it to fully configure the HTTP response
⦁	If we want to use it, we have to return it from the endpoint; Spring takes care of the rest.
⦁	ResponseEntity is generic type. Consequently , We can use any type as the response body
**Example 1**
@GetMapping("student")
    public ResponseEntity<Student> getStudent(){
        Student student = new Student(1, "Pankaj", "Ray");
        return new ResponseEntity<>(student, HttpStatus.OK);
        //return ResponseEnity.OK(student)
        // Both the statement are same ResponseEnity having OK() as static method
        //return ResponseEntity.ok().header("custom-header", "pankaj").body(student);
        // this will return header with ResponseEntity
        
    }
    
**Example 2**
  //Using ResponseEntity class
    @GetMapping("students")
    public ResponseEntity<List<Student>> getStudents(){
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Pankaj", "Ray"));
        students.add(new Student(1, "Raju", "Ray"));
        students.add(new Student(1, "Bablu", "Ray"));
        
        return ResponseEntity.ok(students);

**Example 3**
 @PostMapping("students/create")
    public ResponseEntity<Student> createStudent(@RequestBody Student student){
        System.out.println(student.getId());
        System.out.println(student.getFirstName());
        System.out.println(student.getLastName());
        return ResponseEntity<>(student, HttpStatus.CREATED);
    }


**Define Base URL for REST API's in Spring MVC Controller (@RequestMapping)**

If we can notice StudentController almost all the API has same base url "students" so instead of repeating same base url in each API why not keep it in a single place at class level

Annotate RestController class as
@RequestMapping("students")
and remove it from method level
