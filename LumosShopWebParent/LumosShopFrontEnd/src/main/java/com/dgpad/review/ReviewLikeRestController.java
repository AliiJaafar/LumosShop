package com.dgpad.review;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.dgpad.interactions.InteractionRepository;
import com.dgpad.product.ProductService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.interactions.Interaction;
import com.lumosshop.common.entity.interactions.InteractionType;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.entity.review.LikeConsequence;
import com.lumosshop.common.entity.review.LikeEnum;
import com.lumosshop.common.entity.review.Review;
import com.lumosshop.common.exception.CustomerNotFoundException;
import com.lumosshop.common.exception.ProductNotFoundException;
import com.lumosshop.common.exception.ReviewNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ReviewLikeRestController {

    @Autowired
    private ReviewLikeService likeService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private InteractionRepository interactionRepository;

    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        return customerService.getCustomerByEmail(email);
    }

    @PostMapping("/react/{ID}/{enum}")
    public LikeConsequence actingWithReview(HttpServletRequest httpServletRequest,
                                            @PathVariable(name = "ID")Integer reviewID,
                                            @PathVariable(name = "enum")String likeEnum) throws CustomerNotFoundException, ProductNotFoundException, ReviewNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        if (customer == null) {
            return LikeConsequence.unsuccessful("Only login-users can react on any review");
        }
        Review review = reviewService.retrieveReview(reviewID);

        Interaction interaction = new Interaction();
        interaction.setInteractionType(InteractionType.REVIEW_LIKE);
        interaction.setProduct(review.getProduct());
        interaction.setTimestamp(new Date());
        interaction.setValue(1);
        interaction.setCustomer(customer);
        interactionRepository.saveOrIncrementValue(interaction);
        LikeEnum likeENUM =LikeEnum.valueOf(likeEnum.toUpperCase());
        return likeService.like(customer, likeENUM, reviewID);
    }
}
