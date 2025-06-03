package com.martin.jpa.controller;

import com.martin.jpa.model.Customer;
import com.martin.jpa.repository.CustomerRepository;
import com.martin.jpa.service.CustomerService;
import jakarta.validation.Valid;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService; // alternatively, use constructor to instantiate

    @PostMapping("/add")
    public ResponseEntity<Object> addCustomer(@Valid @RequestBody Customer customer) throws Exception{
        try{
            // try to save the customer to the database
            customerService.save(customer);
            return new ResponseEntity<>(customer, HttpStatus.CREATED);
        }catch (Exception e){
            // present the error back to the api call
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> allCustomers() throws Exception{
        try {
            // retrieve all customers
            List<Customer> customers = customerService.findAll();

            // if no customer is returned
            if(customers.isEmpty())
                throw new Exception("No customer found.");

            return new ResponseEntity<>(customers, HttpStatus.OK); // 200
        }catch (Exception e){
            // present the error back to the api call
            if(e.getMessage().equals("No customer found."))
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400
        }
    }

    @PutMapping("/update/{id}") // Path variable
    public ResponseEntity<Object> updateCustomer(@PathVariable("id") Integer id, @RequestBody Customer customer) throws Exception{
        try{
            // find the customer, else throw an exception
            Customer currentCustomer = customerService.findById(id).orElseThrow(()->new Exception("Customer not found."));

            // update the customer
            currentCustomer.setFirstName(customer.getFirstName());
            currentCustomer.setLastName(customer.getLastName());
            currentCustomer.setEmail(customer.getEmail());
            currentCustomer.setPhone(customer.getPhone());

            Customer result = customerService.update(currentCustomer);
            return new ResponseEntity<>(result, HttpStatus.OK); // or use NO_CONTENT

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable("id") Integer id) throws Exception {

        try{
            // get customer by id and return the response
            Customer customer = customerService.findById(id).orElseThrow(()->new Exception("Customer not found."));
            return new ResponseEntity<>(customer, HttpStatus.OK); // 200
        }catch(Exception e){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomerById(@PathVariable("id") Integer id) throws Exception{

        try {
            // delete the customer only if the customer is found
            Customer customer = customerService.findById(id).orElseThrow(()->new Exception("Unable to perform the task."));

            customerService.delete(customer.getId());

            return new ResponseEntity<>(customer, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getCustomerByEmailOrLastName(
            @RequestParam("email") String email,                            // email is a url param
            @RequestParam("lastName") String lastName) throws Exception{    // lastName is a url param

        try {

            if(!email.isBlank() && !lastName.isBlank()) {                   // email and lastName params found

                List<Customer> customers = customerService.
                        findByEmailContainingOrLastNameContaining(email, lastName);

                if (customers.isEmpty())
                    throw new Exception("Customer not found.");

                return new ResponseEntity<>(customers, HttpStatus.OK);

            }else if(!email.isBlank() && lastName.isBlank()){               // only email param found

                List<Customer> customers = customerService.findByEmailContaining(email);

                if (customers.isEmpty())
                    throw new Exception("Customer not found.");

                return new ResponseEntity<>(customers, HttpStatus.OK);

            }else if(email.isBlank() && !lastName.isBlank()){               // only lastName param found

                List<Customer> customers = customerService.findByLastNameContaining(lastName);

                if (customers.isEmpty())
                    throw new Exception("Customer not found.");

                return new ResponseEntity<>(customers, HttpStatus.OK);
            }else{                                                          // no params found, return everything
                return new ResponseEntity<>(allCustomers(), HttpStatus.OK);
            }

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
