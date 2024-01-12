package com.dgpad.recommender.demographic;

import com.dgpad.order.OrderService;
import com.dgpad.product.ProductRepository;
import com.dgpad.shoppingBag.ShoppingBagService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.ItemSet;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerSegmentationRestController {

    @Autowired
    private SegmentationService segmentationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private ShoppingBagService shoppingBagService;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/byCity/{city}")
    public ResponseEntity<List<Customer>> getCustomersByCity(@PathVariable String city) {
        List<Customer> customers = segmentationService.getCustomerInCity(city);
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/byCountry/{country}")
    public ResponseEntity<List<Customer>> getCustomersByCountry(@PathVariable String country) {
        List<Customer> customers = segmentationService.getCustomerInCountry(country);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Object[]>> getPopularProducts() {
        List<Object[]> popularProducts = segmentationService.getPopularProducts();
        return ResponseEntity.ok(popularProducts);
    }


/*    @GetMapping("/recommendations/{CustomerId}")
    public List<ItemSet> getRecommendations(@PathVariable Integer CustomerId) {
        List<Order> customerOrders = orderService.listAllOrdersForCertainCustomer(CustomerId);
        return segmentationService.generateFrequentItemSets(customerOrders, 3);
    }*/

    @GetMapping("/recommendationsForCustomer/{customerId}")
    public ResponseEntity<List<String>> getRecommendationsForCustomer(@PathVariable Integer customerId) {
        List<Order> customerOrders = orderService.listAllOrdersForCertainCustomer(customerId);
        List<Product> InCartProducts = shoppingBagService.AllProductInCartForCustomer(customerId);
        List<String> recommendations = recommendationService.getTopNRecommendations(customerOrders, InCartProducts,5);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/recommendations/{customerId}")
    public ResponseEntity<List<String>> getRecommendations(@PathVariable Integer customerId) {

        // Get all orders for the customer
        List<Order> allOrder = orderService.displayAllOrders();
        List<Product> inCartProducts = shoppingBagService.AllProductInCartForCustomer(customerId);
        System.out.println("In-Cart Products: "+ inCartProducts);
        // Get top N recommendations
        List<String> recommendations = recommendationService.getTopNRecommendations(allOrder, inCartProducts, 3);
        List<Product> recommendationAsProduct = productRepository.findByNameIn(recommendations);
        System.out.println("recommendations: -> " + recommendations);
        System.out.println("recommendationAsProduct: -> " + recommendationAsProduct);
        return ResponseEntity.ok(recommendations);
    }
}
