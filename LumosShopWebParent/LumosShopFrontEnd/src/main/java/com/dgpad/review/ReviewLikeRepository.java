package com.dgpad.review;

import com.lumosshop.common.entity.review.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Integer> {


    @Query("select l from ReviewLike l where l.customer.id =?1 and l.review.product.id = ?2")
    public List<ReviewLike> findAllByCustomerAndProduct(Integer customerID,Integer productID);
    @Query("select l from  ReviewLike l where l.customer.id = ?2 and l.review.id = ?1")
    public ReviewLike findByReviewAndCustomer(Integer reviewID, Integer CustomerID);



}
