package com.dgpad.admin.review;

import com.dgpad.admin.product.ProductRepository;
import com.lumosshop.common.entity.Review;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.exception.OrderNotFoundException;
import com.lumosshop.common.exception.ReviewNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    public static final int ITEM_IN_PAGE = 6;


    public List<Review> findAll() {
        return (List<Review>) reviewRepository.findAll();
    }


    public Page<Review> listByPage(int pageNumber,
                                       String sortField,
                                       String sortDirection,
                                       String Keyword) {

        Sort sort = null;
        sort = Sort.by(sortField);


        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, ITEM_IN_PAGE, sort);


        if (Keyword != null) {
            return reviewRepository.findAll(Keyword, pageable);
        }
        return reviewRepository.findAll(pageable);
    }

    public Review findReviewById(int id) throws ReviewNotFoundException {
        try {
            return reviewRepository.findById(id).get();

        } catch (NoSuchElementException e) {
            throw new ReviewNotFoundException("Could not found this review id -  " + id);
        }

    }
    public void deleteById(int id) throws ReviewNotFoundException {


        if (!reviewRepository.existsById(id)) {

            throw new ReviewNotFoundException("could not found this review id - " + id);
        }
        reviewRepository.deleteById(id);

    }

    public void save(Review reviewInSite) {
        Review storedReview = reviewRepository.findById(reviewInSite.getId()).get();
        storedReview.setTitle(reviewInSite.getTitle());
        storedReview.setReviewComment(reviewInSite.getReviewComment());

        reviewRepository.save(storedReview);
        productRepository.improveRatingsAndAverageScore(storedReview.getProduct().getId());
    }
}
