** Spring Boot Restful Webservices(CRUD Operaion) using MySQL**


Added Below dependency


**Configure MySQL database in Springboot application**
step 0: Add MySQL Driver dependency in pom.xml
step1: create database in MySQL
        create database user_management;

step3: Add below properties in application.properties file

        #JBC url to connect with url
        #user_management is database name which we have already created in MySQL
        spring.datasource.url=jdbc:mysql://localhost:3306/user_management
        spring.datasource.username=root
        spring.datasource.password=Munnu@10Oct

        #Hibernare Dialect: Hibernate will use this dialect to create appropriate SQL statement with respect to database
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
        #This property tells Hibernate that create table automatically if tables are not exists. I
        #if tables are already in the database then change or alter the table as per the jpa entity mapping changes 
        spring.jpa.hibernate.ddl-auto=update


**Creating User JPA Entity**

        package com.pankajdets.springootrestfulwebservices.model;

        import jakarta.persistence.Column;
        import jakarta.persistence.Entity;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;
        import jakarta.persistence.Table;
        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

        @Getter //lombok dependency
        @Setter  //lombok dependency
        @NoArgsConstructor  //lombok dependency
        @AllArgsConstructor  //lombok dependency
        @Entity  // Specifies that this class is a JPA Entity
        @Table(name = "users")   //To configure table details. By Defaukt ist will take table name as class name
        public class User {
            @Id //to make it primary key
            @GeneratedValue(strategy = GenerationType.IDENTITY) // This internally uses auto increment feature to increment Id
            private long id;
            @Column(nullable = false)  // To customize column. If column name has two word then JPA automatically convert it to "first_name"
            private String firstName;
            @Column(nullable = false)
            private String lastName;
            @Column(nullable = false, unique = true)
            private String email;
        }

Now after running application users table is created in MySQL user_management database

**Create UserRepository which extends JpaRepository**

public interface UserRepository extends JpaRepository<User, Long> {
    
}


