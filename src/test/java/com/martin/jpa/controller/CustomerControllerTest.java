package com.martin.jpa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.jpa.model.Customer;
import com.martin.jpa.repository.CustomerRepository;
import com.martin.jpa.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // disable security filters while testing
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "";

    private Customer customer1, customer2;
    private final List<Customer> customerList = new ArrayList<>();

    // Run setup before each Unit Test is performed
    @BeforeEach
    void setUp() {

        // delete all customer records before each test
        customerRepository.deleteAll();

        // arrange - precondition
        customer1 = new Customer("Jim", "Doe", "jimdoe@gmail.com", "");
        customer2 = new Customer("Sam", "Smith", "samsmith@yahoo.com", "");

        customerList.add(customer1);
        customerList.add(customer2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("**JUNIT test: add a customer from CustomerController**")
    void addCustomer() throws Exception {
        // arrange - prepare
        String requestBody = objectMapper.writeValueAsString(customer1);

        // act - action or behaviour
        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT.concat("/add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // assert - verify the output
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(customer1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer1.getLastName()))
                .andExpect(jsonPath("$.email").value(customer1.getEmail()));
    }

    @Test
    @DisplayName("**JUNIT test: get all customers from CustomerController**")
    void allCustomers() throws Exception {
        // arrange - prepare
        customerRepository.saveAll(customerList);

        // act - action or behaviour
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/all")));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(customerList.size())));
    }

    @Test
    @DisplayName("**JUNIT test: update customer from CustomerController**")
    void updateCustomer() throws Exception {
        // arrange - prepare
        customerService.save(customer1);

        Customer customer = customerService.findById(customer1.getId()).get();

        customer.setFirstName("jim_updated");
        customer.setLastName("doe_updated");
        customer.setEmail("jimdoe_updated@gmail.com");
        customer.setPhone("12345678");

        String requestBody = objectMapper.writeValueAsString(customer);

        // act - action or behaviour
        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/update/{id}"), customer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the results
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(customer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer.getLastName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()))
                .andExpect(jsonPath("$.phone").value(customer.getPhone()));
    }

    @Test
    @DisplayName("**JUNIT test: get customer by ID from CustomerController**")
    void getCustomerById() throws Exception {
        // arrange - prepare
        customerService.save(customer1);

        // act - action and behaviour
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/{id}"), customer1.getId()));

        // assert - verify the results
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(customer1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer1.getLastName()))
                .andExpect(jsonPath("$.email").value(customer1.getEmail()))
                .andExpect(jsonPath("$.phone").value(customer1.getPhone()));
    }

    @Test
    @DisplayName("**JUNIT test: delete customer by ID from CustomerController**")
    void deleteCustomerById() throws Exception {
        // arrange - prepare
        customerService.save(customer2);

        // act - action or behaviour
        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), customer2.getId()));

        // assert - verify the results
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(customer2.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer2.getLastName()))
                .andExpect(jsonPath("$.email").value(customer2.getEmail()))
                .andExpect(jsonPath("$.phone").value(customer2.getPhone()));
    }

    @Test
    @DisplayName("**JUNIT test: search for customer by email or lastname from CustomerController**")
    void getCustomerByEmailOrLastName() throws Exception {
        // arrange - prepare
        customerRepository.saveAll(customerList);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        requestParams.add("email", customer1.getEmail());
        requestParams.add("lastName", customer1.getLastName());

        // act - action or behaviour
        ResultActions resultActions = mockMvc
                .perform(get(API_ENDPOINT).params(requestParams).contentType(MediaType.APPLICATION_JSON));

        // assert - verify the results
        // the test returns an Array, assertions should be tested against the 1st element (index 0)
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].firstName").value(customer1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(customer1.getLastName()))
                .andExpect(jsonPath("$[0].email").value(customer1.getEmail()))
                .andExpect(jsonPath("$[0].phone").value(customer1.getPhone()));


    }
}