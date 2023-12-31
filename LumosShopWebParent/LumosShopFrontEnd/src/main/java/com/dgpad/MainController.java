package com.dgpad;

import com.dgpad.category.CategoryService;
import com.dgpad.customer.CustomerService;
import com.dgpad.product.ProductService;
import com.dgpad.recommender.RecommenderService;
import com.lumosshop.common.entity.Category;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RecommenderService recommenderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        if (email == null) {
            throw new CustomerNotFoundException("Visitor");
        }
        return customerService.getCustomerByEmail(email);
    }
    @GetMapping(value = "")
    public String viewHomePage(Model model,HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        // Assuming you have a method to calculate item similarities
        Map<Integer, Map<Integer, Double>> itemSimilarities = recommenderService.calculateAndUseItemSimilarities();

        // Assuming you have a method to create the user-item matrix
        Map<Integer, Map<Integer, Double>> userItemMatrix = recommenderService.createUserItemMatrix();

        // Generate recommendations for the user
        List<Integer> recommendations = recommenderService.generateRecommendationsForUser(customer.getId(), userItemMatrix, itemSimilarities);

        // Filter products based on recommendations
        List<Product> filteredProducts = productService.listAllProducts().stream()
                .filter(product -> recommendations.contains(product.getId()))
                .collect(Collectors.toList());


        model.addAttribute("customer", customer.getFullName());
        // Pass the recommendations to the view
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("products", filteredProducts);

        // Return the Thymeleaf template name (recommendations.html)







        model.addAttribute("title","LUMOS - HOME");

        List<Category> categoryList = categoryService.findLeafCategories();
        model.addAttribute("categoryList", categoryList);
        return "index";
    }
    @GetMapping("/login")
    public String viewLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";

    }
}
