package com.martin.jpa.service;

import com.martin.jpa.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerServiceInterface {

    // create method signatures
    public Customer save(Customer customer);
    public List<Customer> findAll();
    public Customer update(Customer customer);
    public Optional<Customer> findById(Integer id);
    public void delete(Integer id);

    // TODO
    public  List<Customer> findByEmailContainingOrLastNameContaining(
            String email,
            String lastName
    );

    public List<Customer> findByEmailContaining(String email);

    public List<Customer> findByLastNameContaining(String lastName);

}
