package com.martin.jpa.repository;

import com.martin.jpa.model.Customer;
import com.martin.jpa.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    //save()
    //findById()
    //deleteById()

    // custom query
    @Modifying          // essential for custom queries that delete or update records
    @Transactional      // essential to ensure that the operation is within one transaction
    @Query(value="DELETE FROM feedback WHERE customer_id = :id", nativeQuery = true)
    void deleteByCustomerId(@Param("id") Integer id);

    // derived query
    List<Feedback> findByCustomer(Customer customer);
}
