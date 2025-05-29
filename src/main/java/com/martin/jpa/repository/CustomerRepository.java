package com.martin.jpa.repository;

import com.martin.jpa.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    // save()
    // findById(Integer)
    // findAll()
    // count()
    // delete(Customer Object)
    // delete(Integer)

    // find customer by email containing specific value(s)
    // using DERIVED queries
    List<Customer> findByEmailContainingOrLastNameContaining(String email, String lastName);
    List<Customer> findByEmailContaining(String email);
    List<Customer> findByLastNameContaining(String lastName);
}
