package com.dgpad.admin.review;

import com.lumosshop.common.entity.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Integer>, CrudRepository<Review, Integer> {


    @Query("select r from Review r " +
            "where r.title like %?1% " +
            "or r.ReviewComment like %?1% " +
            "or r.product.name like %?1%" +
            "or concat(r.customer.firstName,' ',r.customer.lastName)like %?1%")
    public Page<Review> findAll(String keyword, Pageable pageable);


}
