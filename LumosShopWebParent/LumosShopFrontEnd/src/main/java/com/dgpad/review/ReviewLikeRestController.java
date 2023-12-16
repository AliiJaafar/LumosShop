package com.dgpad.review;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.review.LikeConsequence;
import com.lumosshop.common.entity.review.LikeEnum;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewLikeRestController {

    @Autowired
    private ReviewLikeService likeService;
    @Autowired
    private CustomerService customerService;

    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        if (email == null) {
            throw new CustomerNotFoundException("Visitor");
        }
        return customerService.getCustomerByEmail(email);
    }

    @PostMapping("/react/{ID}/{enum}")
    public LikeConsequence actingWithReview(HttpServletRequest httpServletRequest,
                                            @PathVariable(name = "ID")Integer reviewID,
                                            @PathVariable(name = "enum")String likeEnum) throws CustomerNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        if (customer == null) {
            return LikeConsequence.unsuccessful("Only login-users can react on any review");
        }
        LikeEnum likeENUM =LikeEnum.valueOf(likeEnum.toUpperCase());
        return likeService.like(customer, likeENUM, reviewID);
    }
}
