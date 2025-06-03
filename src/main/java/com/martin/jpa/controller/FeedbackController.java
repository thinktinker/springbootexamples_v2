package com.martin.jpa.controller;

import com.martin.jpa.exception.ResourceNotFoundException;
import com.martin.jpa.model.Customer;
import com.martin.jpa.model.Feedback;
import com.martin.jpa.service.CustomerService;
import com.martin.jpa.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    CustomerService customerService;

    @PostMapping("/add/{customer_id}")      // customer_id rep. which customer saved the feedback
    public ResponseEntity<Object> saveFeedback(
            @PathVariable("customer_id") Integer customer_id,
            @Valid @RequestBody Feedback feedback) throws ResourceNotFoundException {

            Feedback checkFeedback = customerService.findById(customer_id).map((customer) -> {
                Feedback _feedback = new Feedback(customer, feedback.getDescription());
                return feedbackService.saveFeedback(_feedback);
            }).orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

            return new ResponseEntity<>(checkFeedback, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Object> allFeedback() throws ResourceNotFoundException{

            List<Feedback> feedbackList = feedbackService.findAll();

            if(feedbackList.isEmpty())
                throw new ResourceNotFoundException("No feedback found.");

            return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    @PutMapping("/{feedback_id}")
    public ResponseEntity<Object> updateFeedback(
            @PathVariable("feedback_id")Integer feedback_id,
            @Valid @RequestBody Feedback feedback
    ) throws ResourceNotFoundException{

            Feedback checkFeedback = feedbackService.findById(feedback_id).map((_feedback)->{
                _feedback.setDescription(feedback.getDescription());
                return feedbackService.saveFeedback(_feedback);
            }).orElseThrow(()->new ResourceNotFoundException("There was an error."));

            return new ResponseEntity<>(checkFeedback, HttpStatus.OK);
    }

    @DeleteMapping("/{feedback_id}")
    public ResponseEntity<Object> deleteFeedback(@PathVariable("feedback_id") Integer feedback_id) throws ResourceNotFoundException{

            Feedback checkFeedback = feedbackService.findById(feedback_id).map((_feedback)->{
                feedbackService.deleteById(_feedback.getId());
                return _feedback;
            }).orElseThrow(()->new ResourceNotFoundException("There was an error."));

            return new ResponseEntity<>(checkFeedback, HttpStatus.OK); // 200
    }

    @GetMapping("/count")
    public ResponseEntity<Object> countFeedback(){
            return new ResponseEntity<>(feedbackService.count(), HttpStatus.OK);
    }

    // Challenge statement
    // Delete the feedback that belongs to a customer

    // 1. Find the customer
    // 2. Delete the customer's feedback

    @DeleteMapping("/customer/{customer_id}")
    public ResponseEntity<Object> deleteFeedbackByCustomer(@PathVariable("customer_id") Integer customer_id) throws ResourceNotFoundException{

            Customer customer = customerService.findById(customer_id).map((_customer)->{
                feedbackService.deleteByCustomerId(_customer.getId());
                return _customer;
            }).orElseThrow(()-> new ResourceNotFoundException("There was an error."));

            return new ResponseEntity<>(String.format("Deleted all feedback from customer id: %d", customer.getId()), HttpStatus.OK);

    }

    @GetMapping("/customer/{customer_id}")
    public ResponseEntity<Object> getFeedbackByCustomerId(@PathVariable("customer_id") Integer customer_id) throws ResourceNotFoundException{

            List<Feedback> checkFeedback = customerService.findById(customer_id).map((_customer)->{
                return feedbackService.findByCustomer(_customer);
            }).orElseThrow(()-> new ResourceNotFoundException("There was an error."));

            return new ResponseEntity<>(checkFeedback, HttpStatus.OK);
    }
}

