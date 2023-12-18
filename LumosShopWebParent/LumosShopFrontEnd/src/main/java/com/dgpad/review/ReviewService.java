package com.dgpad.review;

import com.dgpad.order.OrderSummaryRepository;
import com.dgpad.product.ProductRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.review.Review;
import com.lumosshop.common.entity.order.Order_Phase;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.ReviewNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
public class ReviewService {
    public static final int ITEM_IN_PAGE = 6;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderSummaryRepository orderSummaryRepository;


    public Review save(Review review) {
        review.setReviewDate(new Date());
        Review theReview = reviewRepository.save(review);

        Integer productID = theReview.getProduct().getId();
//        productRepository.improveRatingsAndAverageScore(productID);
            return theReview;
    }
    public Page<Review> listingAllReviews(Customer customer, String keyword, String sortField,
                                          String sortDirection, int pageNumber) {

        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNumber - 1, ITEM_IN_PAGE, sort);
        if (keyword != null) {
            return reviewRepository.findByCustomer(customer.getId(), keyword, pageable);
        }
        return reviewRepository.findByCustomer(customer.getId(), pageable);

    }

    public Review retrievingUsingIDAndCustomer(Customer customer,Integer ReviewID) throws ReviewNotFoundException {
        Review review = reviewRepository.findByIdAndCustomer(ReviewID, customer.getId());
        if (review == null) {
            throw new ReviewNotFoundException("There are no reviews associated with the customer for the provided ID " + ReviewID + ".");
        }
        return review;

    }

    public Page<Review> displayByCertainProduct(Product product, String sortField,
                                                String sortDirection, int pageNumber) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber - 1, ITEM_IN_PAGE, sort);

        return reviewRepository.findByProduct(product, pageable);

    }
    public Page<Review> displayLastThreeReviews(Product product) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(0, 3, sort);

        return reviewRepository.findByProduct(product, pageable);

    }
    public Page<Review> displayTheMostThreeLikedReviews(Product product) {
        Sort sort = Sort.by("likes").descending();
        Pageable pageable = PageRequest.of(0, 3, sort);

        return reviewRepository.findByProduct(product, pageable);

    }

    public boolean theCustomerAbleToWriteReview(Integer productID,
                                             Customer customer) {
        Long i = orderSummaryRepository.getProductCountForCustomerByOrderStatus(Order_Phase.RECEIVED, customer.getId(), productID);
        return i > 0;
    }

    public boolean isSuchProductReviewed(Integer productID, Customer customer) {
        Long i = reviewRepository.countByProductAndCustomer(productID, customer.getId());
        return i == 1;
    }

}
