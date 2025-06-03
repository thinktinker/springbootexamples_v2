package com.martin.jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)             // primary key, auto-incremented
    Integer Id;

    @Column(nullable = false)
    @NotBlank(message = "First name must not be blank.")
    @Size(min=2, message = "Min 2 characters for the first name.")
    String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Last name must not be blank.")
    @Size(min=3, message = "Min 3 characters for the last name.")
    String lastName;

    @Column(nullable = false, unique = true)                    // prevents null values & email duplicates
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",  // regular expression for email
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Email is invalid."
    )
    String email;

    @Column
    @Pattern(regexp = "^\\d{8}$", message = "Phone number must be 8 digits only.")
    String phone;

    public Customer() {                                                                 // empty constructor
    }

    public Customer(String firstName, String lastName, String email, String phone) {    // parameterized constructor
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    // getters
    public Integer getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // setters (except id)
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
