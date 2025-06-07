package com.martin.jpa.service;

import com.martin.jpa.exception.ResourceNotFoundException;
import com.martin.jpa.model.Customer;
import com.martin.jpa.model.Feedback;
import com.martin.jpa.repository.CustomerRepository;
import com.martin.jpa.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService implements FeedbackServiceInterface {

    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Override
    public Feedback saveFeedback(Feedback feedback) {

        // the bearer token is passed in and used by authentication
        Authentication authentication = authenticateUser();

        return customerRepository.findByEmail(authentication.getName()).map((_customer)->{
            Feedback _feedback = new Feedback(_customer, feedback.getDescription());
            return feedbackRepository.save(_feedback);
        }).orElseThrow(()->new ResourceNotFoundException("Customer not found."));

    }

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Optional<Feedback> findById(Integer id) {

        return feedbackRepository.findById(id);
    }

    @Override
    public Feedback updateFeedback(Integer id, Feedback feedback) {

        // the bearer token is passed in and used by authentication
        Authentication authentication = authenticateUser();

        // feedback should only be updated by the authenticated user who submitted the feedback
        Feedback updateFeedback = customerRepository.findByEmail(authentication.getName()).map((customer)->{
            return feedbackRepository
                    .findByIdAndCustomer(id, customer)
                    .orElseThrow(()->new ResourceNotFoundException("No feedback found."));
        }).orElseThrow(()->new ResourceNotFoundException("Customer not found."));

        updateFeedback.setDescription(feedback.getDescription());
        return feedbackRepository.save(updateFeedback);
    }

    @Override
    public void deleteById(Integer id) {

        // the bearer token is passed in and used by authentication
        Authentication authentication = authenticateUser();

        // all feedback deleted should only belong to the authenticated user
        Feedback deleteFeedback = customerRepository.findByEmail(authentication.getName()).map((customer)->{
            return feedbackRepository
                    .findByIdAndCustomer(id, customer)
                    .orElseThrow(()->new ResourceNotFoundException("No feedback found."));
        }).orElseThrow(()->new ResourceNotFoundException("Customer not found."));

        feedbackRepository.deleteById(deleteFeedback.getId());
    }

    public List<Feedback> findByCustomer(){

        // the bearer token is passed in and used by authentication
        Authentication authentication = authenticateUser();

        // feedback should only be those of the authenticated user who submitted the feedback
        return customerRepository.findByEmail(authentication.getName()).map((customer)->{
            return feedbackRepository.findByCustomer(customer);
        }).orElseThrow(()->new ResourceNotFoundException("Customer not found."));

    }

    @Override
    public long count() {

        // the bearer token is passed in and used by authentication
        Authentication authentication = authenticateUser();

        // return only the number of feedback (size) belonging to the authenticated user
        return customerRepository.findByEmail(authentication.getName()).map((customer)->{
            return feedbackRepository.findByCustomer(customer).size();
        }).orElseThrow(()->new ResourceNotFoundException("Customer not found."));

    }

    public Customer deleteByCustomer(){

        // the bearer token is passed in and used by authentication
        Authentication authentication = authenticateUser();

        // delete all feedback belonging to the authenticated user
        Customer customer = customerRepository.findByEmail(authentication.getName())
                .orElseThrow(()->new ResourceNotFoundException("Customer not found."));

        feedbackRepository.deleteByCustomerId(customer.getId());

        return customer;
    }

    private Authentication authenticateUser(){
        // the bearer token is passed in and used by authentication
        // to extract the authenticated user
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

















