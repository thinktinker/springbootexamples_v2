package com.martin.jpa.service;

import com.martin.jpa.model.Customer;
import com.martin.jpa.model.Feedback;
import com.martin.jpa.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService implements FeedbackServiceInterface {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
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
    public Feedback updateFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public void deleteById(Integer id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public long count() {
        return feedbackRepository.count();
    }

    public void deleteByCustomerId(Integer id){
        feedbackRepository.deleteByCustomerId(id);
    }

    public List<Feedback> findByCustomer(Customer customer){
        return feedbackRepository.findByCustomer(customer);
    }
}

















