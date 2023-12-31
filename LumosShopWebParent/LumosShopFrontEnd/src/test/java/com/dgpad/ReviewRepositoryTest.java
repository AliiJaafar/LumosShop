package com.dgpad;

import com.dgpad.customer.CustomerRepository;
import com.dgpad.interactions.InteractionRepository;
import com.dgpad.product.ProductRepository;
import com.dgpad.review.ReviewRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.interactions.Interaction;
import com.lumosshop.common.entity.interactions.InteractionType;
import com.lumosshop.common.entity.review.Review;
import com.lumosshop.common.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private InteractionRepository interactionRepository;

    @Test
    public void testFindProductByID() {
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();
        System.out.println(product);

    }

    @Test
    public void testListAllInteractions() {
        Iterable<Interaction> interactionIterable = interactionRepository.findAll();

        interactionIterable.forEach(System.out::println);
    }
    @Test
    public void testListAllInteractionsByProduct() {
        Product product = productRepository.findById(4).get();
        Iterable<Interaction> interactionIterable = interactionRepository.findAllByProduct(product);

        interactionIterable.forEach(System.out::println);
    }
    @Test
    public void testListAllInteractionsByCustomer() {
        Customer customer = customerRepository.findById(35).get();
        Iterable<Interaction> interactionIterable = interactionRepository.findAllByCustomer(customer);

        interactionIterable.forEach(System.out::println);
    }
    @Test
    public void testListAllInteractionsByType() {
        Iterable<Interaction> interactionIterable = interactionRepository.findAllByInteractionType(InteractionType.CLICK);

        interactionIterable.forEach(System.out::println);
    }

    @Test
    public void improveTheReviewLikeTable() {
        Integer reviewID = 3;
        reviewRepository.updateLikes(reviewID);

        Review review = reviewRepository.findById(reviewID).get();
    }

    @Test
    public void retrieveNumOfLikesTest() {
        Integer reviewID = 3;
        Integer likes = reviewRepository.retrieveNumbersOfLikes(reviewID);
        System.out.println(likes + " Is NUMBER OF LIKES in REVIEW #" + reviewID + "\n------------------\n-----------");
    }
}
