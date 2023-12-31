package com.dgpad.recommender;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.dgpad.product.ProductService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MatrixController {

    @Autowired
    private  RecommenderService recommenderService;
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


    @GetMapping("/display-matrix")
    public String displayMatrix(Model model) {
        Map<Integer, Map<Integer, Double>> userItemMatrix = recommenderService.createUserItemMatrix();
        model.addAttribute("userItemMatrix", userItemMatrix);
        return "RecSys/matrix";
    }

    @GetMapping("/item-similarity")
    public String displayItemSimilarity(Model model) {
        // Step 3: Calculate item similarities
        Map<Integer, Map<Integer, Double>> itemSimilarities = recommenderService.calculateAndUseItemSimilarities();

        // Extract item IDs for table header
        model.addAttribute("itemIds", itemSimilarities.keySet());

        // Pass the item similarity matrix to the view
        model.addAttribute("itemSimilarities", itemSimilarities);

        // Return the Thymeleaf template name (itemSimilarity.html)
        return "RecSys/itemSimilarity";
    }

    @GetMapping("/recommendations")
    public String generateRecommendations(HttpServletRequest httpServletRequest, Model model) throws CustomerNotFoundException {
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
        return "RecSys/recommendations";
    }
}
