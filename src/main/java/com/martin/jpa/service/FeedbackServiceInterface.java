package com.martin.jpa.service;

import com.martin.jpa.model.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackServiceInterface {

    // create method signatures
    public Feedback saveFeedback(Feedback feedback);
    public List<Feedback> findAll();
    public Optional<Feedback> findById(Integer id);
    public Feedback updateFeedback(Feedback feedback);
    public void deleteById(Integer id);
    public long count();

}
