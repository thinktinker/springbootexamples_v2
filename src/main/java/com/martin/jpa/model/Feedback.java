package com.martin.jpa.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)    // relationship btwn Feedback and Customer
    @JoinColumn(name = "customer_id", nullable = false)     // Many Feedback comes from a Customer
    @OnDelete(action = OnDeleteAction.CASCADE)              // manages the foreign constraint of parent entity (customer)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // resolve BeanSerialization issue
    Customer customer;

    @Column(nullable = false)
    @NotBlank(message = "Feedback description cannot be blank.")
    String description;

    // * constructor

    public Feedback() {
    }

    public Feedback(Customer customer, String description) {
        this.customer = customer;
        this.description = description;
    }

    // * getters

    public Integer getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getDescription() {
        return description;
    }

    // * setters
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
