package com.martin.jpa.controller;

import com.martin.jpa.exception.ResourceNotFoundException;
import com.martin.jpa.model.Customer;
import com.martin.jpa.model.Feedback;
import com.martin.jpa.repository.FeedbackRepository;
import com.martin.jpa.service.CustomerService;
import com.martin.jpa.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<Object> saveFeedback(@Valid @RequestBody Feedback feedback) throws ResourceNotFoundException {
            return new ResponseEntity<>(feedbackService.saveFeedback(feedback), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Object> allFeedback() throws ResourceNotFoundException{

            List<Feedback> feedbackList = feedbackService.findAll();

            if(feedbackList.isEmpty())
                throw new ResourceNotFoundException("No feedback found.");

            return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    @PutMapping("/{feedback_id}")
    public ResponseEntity<Object> updateFeedback(@PathVariable("feedback_id")Integer feedback_id, @Valid @RequestBody Feedback feedback) throws ResourceNotFoundException{

        // find the feedback
        Feedback checkFeedback = feedbackService.findById(feedback_id).map((currentFeedback)->{
            // if feedback exists, invoke updateFeedback()
            return feedbackService.updateFeedback(currentFeedback.getId(), feedback);
        }).orElseThrow(()->new ResourceNotFoundException("Feedback not found."));

        return new ResponseEntity<>(checkFeedback, HttpStatus.OK);
    }

    @DeleteMapping("/{feedback_id}")
    public ResponseEntity<Object> deleteFeedback(@PathVariable("feedback_id") Integer feedback_id) throws ResourceNotFoundException{

            Feedback checkFeedback = feedbackService.findById(feedback_id).map((currentFeedback)->{
                feedbackService.deleteById(currentFeedback.getId());
                return currentFeedback;
            }).orElseThrow(()->new ResourceNotFoundException("Feedback not found."));

            return new ResponseEntity<>(checkFeedback, HttpStatus.OK); // 200
    }

    @GetMapping("/count")
    public ResponseEntity<Object> countFeedback(){
            return new ResponseEntity<>(feedbackService.count(), HttpStatus.OK);
    }

    @DeleteMapping("/customer")
    public ResponseEntity<Object> deleteFeedbackByCustomer() throws ResourceNotFoundException{

            Customer customer = feedbackService.deleteByCustomer();

            return new ResponseEntity<>(String.format("Deleted all feedback from customer id: %d", customer.getId()), HttpStatus.OK);

    }

    @GetMapping("/customer")
    public ResponseEntity<Object> getFeedbackByCustomerId() throws ResourceNotFoundException{
            return new ResponseEntity<>(feedbackService.findByCustomer(), HttpStatus.OK);
    }
}

