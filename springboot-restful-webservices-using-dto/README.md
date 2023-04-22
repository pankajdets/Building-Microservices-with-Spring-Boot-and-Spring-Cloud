**In previous project "Springboot-restful-webservices" we have implemented CRUD operation
for User**

But Problem with previous project is that we are returing JPA Entity to Client which is not safe
there may be some sensitive information in JPA Entity which we are sending to client hence there is a chance that client can access sensetive information like password

**We can Overcome this Problem using Data Transfer Object (DTO) Pattern**

Data transfer Object(DTO) Pattern is  widely used design pattern to transfer the data between client and server

**Main Advantage of this pattern is :** 
1. To reduce number of remote calls
2. server use DTO to send only the required data to the client


DTOs or Data Transfer Objects are objects that carry data between processes in order to reduce the number of methods calls. The pattern was first introduced by Martin Fowler in his book EAA.

Fowler explained that the pattern's main purpose is to reduce roundtrips to the server by batching up multiple parameters in a single call. This reduces the network overhead in such remote operations.

Another benefit is the encapsulation of the serialization's logic (the mechanism that translates the object structure and data to a specific format that can be stored and transferred). It provides a single point of change in the serialization nuances. It also decouples the domain models from the presentation layer, allowing both to change independently.

DTOs normally are created as POJOs. They are flat data structures that contain no business logic. They only contain storage, accessors and eventually methods related to serialization or parsing.
The data is mapped from the domain models to the DTOs, normally through a mapper component in the presentation or facade layer.

![image](https://user-images.githubusercontent.com/42623098/233796989-0ebed01b-0e71-489d-a125-c6168fcac0d5.png)


4. When to Use It?
DTOs come in handy in systems with remote calls, as they help to reduce the number of them.

DTOs also help when the domain model is composed of many different objects and the presentation model needs all their data at once, or they can even reduce roundtrip between client and server.

With DTOs, we can build different views from our domain models, allowing us to create other representations of the same domain but optimizing them to the clients' needs without affecting our domain design. Such flexibility is a powerful tool to solve complex problems.


**Development Steps for Usecase**
1. Create UserDto Class
2. Refactor Create User REST API to use DTO
3. Create UserMapper class
4. Refactor Get User By Id REST API to use DTO
5. Refactor Get All Users REST API to use DTO
6. Refactor Update User REST API to use DTO
7. Refactor Delete User REST API to use DTO

