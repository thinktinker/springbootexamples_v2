package com.martin.jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="customer", uniqueConstraints = {@UniqueConstraint(name ="email", columnNames = "email")})
public class Customer implements UserDetails {

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

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank.")
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password is not strong.")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role annot be blank.")
    private EnumRole role;

    @Column
    @Pattern(regexp = "^\\d{8}$", message = "Phone number must be 8 digits only.")
    String phone;

    public Customer() {                                                                 // empty constructor
        role = null;
    }

    public Customer(String firstName, String lastName, String email, String phone) {    // parameterized constructor
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = null;
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

    public EnumRole getRole() {
        return role;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(EnumRole role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
