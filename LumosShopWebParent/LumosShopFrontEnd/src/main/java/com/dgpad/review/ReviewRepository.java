package com.dgpad.review;

import com.lumosshop.common.entity.review.Review;
import com.lumosshop.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    public Page<Review> findByProduct(Product product, Pageable pageable);
    @Query("select r from Review r where r.customer.id = ?1")
    public Page<Review> findByCustomer(Integer customerID, Pageable pageable);

    @Query("select r from Review r " +
            "where r.customer.id = ?1 " +
            "and " +
            "(r.title like %?2% " +
            "or r.product.name like %?2% " +
            "or r.ReviewComment like %?2%)")
    public Page<Review> findByCustomer(Integer customerID, String keyword, Pageable pageable);

    @Query("select r from Review r where r.id = ?1 and r.customer.id = ?2")
    public Review findByIdAndCustomer(Integer ReviewID,Integer customerID);

    @Query("select count (r.id) from Review r " +
            "where r.product.id = ?1 " +
            "and r.customer.id = ?2")
    public Long countByProductAndCustomer(Integer productID, Integer customerID);


    @Query("UPDATE Review r " +
            "SET r.likes = " +
            "(SELECT CASE WHEN SUM(l.likes) IS NULL THEN 0 ELSE SUM(l.likes) END " +
            "FROM ReviewLike l WHERE l.review.id = ?1) " +
            "WHERE r.id = ?1")
    @Modifying
    public void updateLikes(Integer reviewID);


    @Query("select r.likes from Review r where r.id = ?1")
    public Integer retrieveNumbersOfLikes(Integer reviewID);


}
