package com.dgpad.admin.review;

import com.lumosshop.common.entity.review.Review;
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

import java.util.List;

@Controller
public class ReviewController {
    @Autowired
    private ReviewService reviewService;


    @GetMapping("/review")
    public String listReviewsForFirstPage(Model model, HttpServletRequest httpServletRequest) {
        return listByPage(model, 1, "id", "asc", null, httpServletRequest);
    }

    @GetMapping("/review/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable(name = "pageNumber") int pageNumber,
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection,
                             @Param("keyword") String keyword,
                             HttpServletRequest httpServletRequest) {

        Page<Review> page = reviewService.listByPage(pageNumber, sortField, sortDirection, keyword);
        List<Review> reviewList = page.getContent();

        long startCount = (long) (pageNumber - 1) * ReviewService.ITEM_IN_PAGE + 1;
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

    @GetMapping("/review/update/{reviewID}")
    public String editReview(@PathVariable("reviewID") Integer reviewID, Model model, RedirectAttributes redirectAttributes) {
        try {
            Review review = reviewService.findReviewById(reviewID);

            model.addAttribute("review", review);
            model.addAttribute("pageTitle", "Edit Review (ID: %d)");

            return "review/review-form";
        } catch (ReviewNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/review";
        }
    }
    @PostMapping("/review/update")
    public String saveChanges(Review reviewInSite, RedirectAttributes redirectAttributes) {
        reviewService.save(reviewInSite);

        redirectAttributes.addFlashAttribute("message", "The review bearing the ID " + reviewInSite.getId() + " has been successfully updated.");
        return "redirect:/review";

    }

    @GetMapping("/review/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws ReviewNotFoundException {
        try {
            reviewService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Review ID " + id + " has been deleted from the system with success.");
        } catch (ReviewNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        // redirect to
        return "redirect:/review";
    }


    @GetMapping("/review/info/{id}")
    public String displayReviewDetail(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {

        try {
            Review review = reviewService.findReviewById(id);
            model.addAttribute("review", review);
            return "review/review-info";
        } catch (ReviewNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/review";
        }
    }
}
