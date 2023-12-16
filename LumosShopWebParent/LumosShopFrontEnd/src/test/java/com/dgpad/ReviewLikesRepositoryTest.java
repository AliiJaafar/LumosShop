package com.dgpad;

import com.dgpad.review.ReviewLikeRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.review.Review;
import com.lumosshop.common.entity.review.ReviewLike;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ReviewLikesRepositoryTest {
    @Autowired
    private ReviewLikeRepository likeRepository;

    @Test
    public void testLikeProduct() {
        Integer reviewID = 3;
        Integer customerID = 12;

        ReviewLike like = new ReviewLike();
        like.setReview(new Review(reviewID));
        like.setCustomer(new Customer(customerID));
        like.like();
        likeRepository.save(like);
    }
}
