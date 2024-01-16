/*
package com.dgpad.recommender;

import com.dgpad.recommender.AprioriAlgorithm;
import com.lumosshop.common.entity.ItemSet;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.product.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.dgpad.recommender.AprioriAlgorithm.countSupport;

@Service
public class RecommendationService {

    public List<String> recommendProducts(List<Order> orders, List<Product> inCartProducts) {

        // Check if the cart is null or empty
        if (inCartProducts == null || inCartProducts.isEmpty()) {
            return Collections.emptyList();  // or return a default list, depending on your logic
        }
        List<ItemSet> frequentItemSets = generateFrequentItemSets(orders, 2);

        // Calculate lift for each product pair
        Map<String, Double> productLiftMap = new HashMap<>();

        // Iterate through frequent itemsets
        for (ItemSet itemSet : frequentItemSets) {
            List<String> items = itemSet.getItems();

            // Check if the itemset has two products
            if (items.size() == 2) {
                String product1 = items.get(0);
                String product2 = items.get(1);

                // Skip if either product is already in the customer's cart
                if (inCartProducts.stream().anyMatch(product -> items.contains(product.getName()))) {
                    continue;
                }

                // Calculate lift considering the in-cart products
                double lift = calculateLift(itemSet, orders, inCartProducts);

                // Update lift for product1
                productLiftMap.put(product1, productLiftMap.getOrDefault(product1, 0.0) + lift);

                // Update lift for product2
                productLiftMap.put(product2, productLiftMap.getOrDefault(product2, 0.0) + lift);
            }
        }

        // Rank products based on total lift
        List<String> recommendedProducts = productLiftMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendedProducts;
    }

    private double calculateLift(ItemSet itemSet, List<Order> orders, List<Product> inCartProducts) {
        int supportXY = countSupport(itemSet, orders);
        int supportX = countSupport(new ItemSet(itemSet.getItems().subList(0, 1)), orders);
        int supportY = countSupport(new ItemSet(itemSet.getItems().subList(1, 2)), orders);

        // Avoid division by zero
        if (supportX == 0 || supportY == 0) {
            return 0.0;
        }

        // Calculate lift considering the intersection of in-cart products and itemset products
        List<String> itemSetProducts = itemSet.getItems();
        Set<String> intersection = inCartProducts.stream()
                .map(Product::getName)
                .filter(itemSetProducts::contains)
                .collect(Collectors.toSet());

        double lift = (double) (supportXY * orders.size()) / (supportX * supportY);

        // Penalize lift if there is no intersection with in-cart products
        if (intersection.isEmpty()) {
            lift *= 0.5; // Penalize by reducing the lift (adjust this factor based on your needs)
        }

        return lift;
    }

    private List<ItemSet> generateFrequentItemSets(List<Order> orders, int minSupport) {
        return AprioriAlgorithm.generateCandidateItemSets(orders, minSupport);
    }

    public List<String> getTopNRecommendations(List<Order> orders, List<Product> inCartProducts, int topN) {
        List<String> recommendedProducts = recommendProducts(orders, inCartProducts);
        return recommendedProducts.subList(0, Math.min(topN, recommendedProducts.size()));
    }



}
*/
