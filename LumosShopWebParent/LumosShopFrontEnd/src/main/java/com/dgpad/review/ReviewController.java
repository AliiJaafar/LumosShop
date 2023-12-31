package com.dgpad.review;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.dgpad.interactions.InteractionRepository;
import com.dgpad.product.ProductService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.interactions.Interaction;
import com.lumosshop.common.entity.interactions.InteractionType;
import com.lumosshop.common.entity.review.Review;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.CustomerNotFoundException;
import com.lumosshop.common.exception.ProductNotFoundException;
import com.lumosshop.common.exception.ReviewNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class ReviewController {


    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ReviewLikeService likeService;
    @Autowired
    private InteractionRepository interactionRepository;

    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        if (email == null) {
            throw new CustomerNotFoundException("Visitor");
        }
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping("/review")
    public String listReviewsForFirstPage(Model model, HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        return listingAllReviews(model, 1, "id", "asc", null, httpServletRequest);
    }

    @GetMapping("/review/page/{pageNumber}")
    public String listingAllReviews(Model model,
                                    @PathVariable(name = "pageNumber") int pageNumber,
                                    @Param("sortField") String sortField,
                                    @Param("sortDirection") String sortDirection,
                                    @Param("keyword") String keyword,
                                    HttpServletRequest httpServletRequest) throws CustomerNotFoundException {

        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        Page<Review> page = reviewService.listingAllReviews(customer, keyword, sortField, sortDirection, pageNumber);
        List<Review> reviewList = page.getContent();
        Long startCount = (long) (pageNumber - 1) * ReviewService.ITEM_IN_PAGE + 1;
        long endCount = startCount + ReviewService.ITEM_IN_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";


        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/review");
        model.addAttribute("pageTitle", "Reviews");
        return "review/reviews";
    }

    @GetMapping("/review/info/{id}")
    public String displayReviewDetail(HttpServletRequest httpServletRequest, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) throws CustomerNotFoundException {

        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        try {
            Review review = reviewService.retrievingUsingIDAndCustomer(customer, id);
            model.addAttribute("review", review);
            return "review/review-info";
        } catch (ReviewNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/review";
        }
    }


    @GetMapping("/review/{alias}")
    public String listProductReviewsForFirstPage(@PathVariable(name = "alias") String alias,
                                                 Model model,
                                                 HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        return listingAllReviewsByCertainProduct(model, alias, 1, "id", "desc", null, httpServletRequest);
    }

    @GetMapping("/review/{alias}/page/{pageNumber}")
    public String listingAllReviewsByCertainProduct(Model model,
                                                    @PathVariable(name = "alias") String alias,
                                                    @PathVariable(name = "pageNumber") int pageNumber,
                                                    @Param("sortField") String sortField,
                                                    @Param("sortDirection") String sortDirection,
                                                    @Param("keyword") String keyword,
                                                    HttpServletRequest httpServletRequest) throws CustomerNotFoundException {

        Product product = null;

        try {
            product = productService.getProduct(alias);
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
        model.addAttribute("product", product);
        Page<Review> page = reviewService.displayByCertainProduct(product, sortField, sortDirection, pageNumber);
        List<Review> reviewList = page.getContent();
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        if (customer != null) {
            likeService.handleActWithReviews(customer.getId(), product.getId(), reviewList);
        }

        Long startCount = (long) (pageNumber - 1) * ReviewService.ITEM_IN_PAGE + 1;
        long endCount = startCount + ReviewService.ITEM_IN_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";


        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("reviews", reviewList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/review");
        model.addAttribute("pageTitle", "Reviews");
        return "review/productPage-reviews";
    }


    @GetMapping("/share-review/{productId}")
    public String displayReviewForm(Model model, @PathVariable("productId") Integer productID,
                                    HttpServletRequest httpServletRequest) throws CustomerNotFoundException {

        Review review = new Review();
        Product product = null;
        try {
            product = productService.retrieveProduct(productID);
        } catch (ProductNotFoundException ex) {
            return "error/404";
        }
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        boolean Reviewed = reviewService.isSuchProductReviewed(productID, customer);

        if (Reviewed) {
            model.addAttribute("Reviewed", true);
        } else {
            boolean ableToWriteReview = reviewService.theCustomerAbleToWriteReview(productID, customer);

            if (ableToWriteReview) {
                model.addAttribute("ableToWriteReview", true);
            } else {
                model.addAttribute("unableToWriteReview", true);
            }
        }
        model.addAttribute("product", product);
        model.addAttribute("review", review);


        return "review/form";
    }

    @PostMapping("/share_review")
    public String saveTheFeedback(Review review,
                                  Integer productId,
                                  HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        Product product = null;

        try {
            product = productService.retrieveProduct(productId);
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
        review.setProduct(product);
        review.setCustomer(customer);

        reviewService.save(review);

        Interaction interaction = new Interaction();
        interaction.setInteractionType(InteractionType.REVIEW);
        interaction.setTimestamp(new Date());
        interaction.setProduct(product);
        interaction.setCustomer(customer);
        interaction.setValue(review.getRating());
        interactionRepository.saveOrIncrementValue(interaction);
        return "redirect:/review/page/1?sortField=id&sortDirection=desc&keyword=" + product.getName();
    }
}
