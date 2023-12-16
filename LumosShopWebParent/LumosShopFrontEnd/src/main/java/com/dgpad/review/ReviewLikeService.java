package com.dgpad.review;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.review.LikeConsequence;
import com.lumosshop.common.entity.review.LikeEnum;
import com.lumosshop.common.entity.review.Review;
import com.lumosshop.common.entity.review.ReviewLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class ReviewLikeService {
    @Autowired
    private ReviewLikeRepository likeRepository;
    @Autowired
    private ReviewRepository reviewRepository;



    public LikeConsequence like(Customer customer, LikeEnum likeEnum, Integer reviewID) {
        Review review = null;

        try {
            review = reviewRepository.findById(reviewID).get();
        } catch (NoSuchElementException e) {
            return LikeConsequence.unsuccessful("The review bearing the identification number #" + reviewID + " is no longer extant.");
        }
        ReviewLike like = likeRepository.findByReviewAndCustomer(reviewID, customer.getId());

        if (like != null) {
            if (like.isLikedAlready() && likeEnum.equals(LikeEnum.LIKE) || like.isDislikedAlready() && likeEnum.equals(LikeEnum.DISLIKE)) {
                return unLike(reviewID, likeEnum, like);
            } else if (like.isDislikedAlready() && likeEnum.equals(LikeEnum.LIKE)) {
                like.like();
            } else if (like.isLikedAlready() && likeEnum.equals(LikeEnum.DISLIKE)) {
                like.dislike();
            }
        } else {
            like = new ReviewLike();
            like.setReview(review);
            like.setCustomer(customer);

            if (likeEnum.equals(LikeEnum.DISLIKE)) {
                like.dislike();
            } else {
                like.like();
            }
        }

        likeRepository.save(like);
        reviewRepository.updateLikes(reviewID);
        Integer likes = reviewRepository.retrieveNumbersOfLikes(reviewID);

        return LikeConsequence.successful(likes, "You've " + likeEnum + " this review.");
    }
    public LikeConsequence unLike(Integer reviewID, LikeEnum likeEnum, ReviewLike reviewLike) {
        likeRepository.delete(reviewLike);
        reviewRepository.updateLikes(reviewID);
        Integer likes = reviewRepository.retrieveNumbersOfLikes(reviewID);

        return LikeConsequence.successful(likes, "You've withdrawn your " + likeEnum + " from the review.");
    }
}
