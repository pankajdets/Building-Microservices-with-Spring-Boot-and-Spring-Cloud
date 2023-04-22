package com.pankajdets.springbootrestfulwebservicesusingdto.model;

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
