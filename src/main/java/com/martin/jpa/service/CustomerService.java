package com.martin.jpa.service;

import com.martin.jpa.model.Customer;
import com.martin.jpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements CustomerServiceInterface{

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Customer save(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return customerRepository.findById(id); // Optional may/may not return anything
    }

    @Override
    public void delete(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findByEmailContainingOrLastNameContaining(String email, String lastName) {
        return customerRepository.findByEmailContainingOrLastNameContaining(
                email, lastName
        );
    }

    @Override
    public List<Customer> findByEmailContaining(String email) {
        return customerRepository.findByEmailContaining(email);
    }

    @Override
    public List<Customer> findByLastNameContaining(String lastName) {
        return customerRepository.findByLastNameContaining(lastName);
    }
}
