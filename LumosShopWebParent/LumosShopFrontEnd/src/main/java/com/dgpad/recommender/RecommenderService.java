package com.dgpad.recommender;

import com.dgpad.interactions.InteractionService;
import com.lumosshop.common.entity.ItemSet;
import com.lumosshop.common.entity.interactions.Interaction;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.dgpad.recommender.AprioriAlgorithm.countSupport;

@Service
public class RecommenderService {

    @Autowired
    private InteractionService interactionService;



    public Map<Integer, Map<Integer, Double>> createUserItemMatrix() {
        Map<Integer, Map<Integer, Double>> userItemMatrix = new HashMap<>();
        Map<Integer, String> itemIdsToNames = new HashMap<>(); // Optional for clarity


        List<Interaction> interactions = interactionService.getAllInteractions();

        for (Interaction interaction : interactions) {
            Integer customerId = interaction.getCustomer().getId();
            Integer productId = interaction.getProduct().getId();
            double value = interaction.getValue();
            double score = calculateScore(interaction, value);

            userItemMatrix
                    .computeIfAbsent(customerId, k -> new HashMap<>())
                    .merge(productId, score, Double::sum);
            // Optionally, store product names for better readability:
            itemIdsToNames.put(productId, interaction.getProduct().getName());
        }

        // If a customer hasn't interacted with a product, set its value to 0:
        for (Map<Integer, Double> itemValues : userItemMatrix.values()) {
            for (Integer itemId : itemIdsToNames.keySet()) {
                itemValues.putIfAbsent(itemId, 0.0);
            }
        }
        return userItemMatrix;
    }
    // Example method to use the ItemSimilarityCalculator
    public Map<Integer, Map<Integer, Double>> calculateAndUseItemSimilarities() {
        // Step 1: Create an instance of the ItemSimilarityCalculator
        ItemSimilarityCalculator similarityCalculator = new ItemSimilarityCalculator();

        // Step 2: Get your user-item matrix
        Map<Integer, Map<Integer, Double>> userItemMatrix = createUserItemMatrix();

        // Step 3: Calculate item similarities
        Map<Integer, Map<Integer, Double>> itemSimilarities = ItemSimilarityCalculator.calculateItemSimilarities(userItemMatrix);

        // Now you can use the item similarities matrix for making recommendations or any other purpose
        // For example, you can print the item similarities matrix
        System.out.println("Item Similarities Matrix:");
        for (Map.Entry<Integer, Map<Integer, Double>> entry : itemSimilarities.entrySet()) {
            Integer itemId1 = entry.getKey();
            Map<Integer, Double> similarityValues = entry.getValue();

            System.out.print("Item " + itemId1 + ": ");
            for (Map.Entry<Integer, Double> similarityEntry : similarityValues.entrySet()) {
                Integer itemId2 = similarityEntry.getKey();
                Double similarity = similarityEntry.getValue();

                System.out.print(itemId2 + "=" + similarity + " ");
            }
            System.out.println();
        }

        // Return or use the item similarities matrix as needed
        return itemSimilarities;
    }



    private double calculateScore(Interaction interaction, double value) {
        switch (interaction.getInteractionType()) {
            case CLICK:
                return value / 100.0;
            case ORDER:
                return value;
            case REVIEW:
                return value / 5.0;
            case REVIEW_LIKE:
                return value / 40.0;
            // Add more cases for other interaction types if needed
            default:
                throw new IllegalArgumentException("Unsupported interaction type");
        }
    }

    // Example method to generate recommendations for a user
    public List<Integer> generateRecommendationsForUser(int userId, Map<Integer, Map<Integer, Double>> userItemMatrix, Map<Integer, Map<Integer, Double>> itemSimilarities) {
        Map<Integer, Double> userVector = userItemMatrix.getOrDefault(userId, Collections.emptyMap());

        Map<Integer, Double> recommendations = new HashMap<>();

        // Iterate over items the user hasn't interacted with
        for (Integer itemId : itemSimilarities.keySet()) {
            if (!userVector.containsKey(itemId) || userVector.get(itemId) == 0.0) {
                double recommendationScore = calculateRecommendationScore(itemId, userId, userItemMatrix, itemSimilarities);
                recommendations.put(itemId, recommendationScore);
            }
        }

        // Sort recommendations by score in descending order
        List<Integer> recommendedItems = new ArrayList<>(recommendations.keySet());
        recommendedItems.sort(Comparator.comparingDouble(recommendations::get).reversed());

        return recommendedItems;
    }

    // Calculate recommendation score for a specific item and user
    private double calculateRecommendationScore(int itemId, int userId, Map<Integer, Map<Integer, Double>> userItemMatrix, Map<Integer, Map<Integer, Double>> itemSimilarities) {
        Map<Integer, Double> itemSimilaritiesWithUser = itemSimilarities.get(itemId);
        Map<Integer, Double> userVector = userItemMatrix.get(userId);

        double numerator = 0.0;
        double denominator = 0.0;

        for (Map.Entry<Integer, Double> entry : userVector.entrySet()) {
            Integer userItem = entry.getKey();
            Double userItemScore = entry.getValue();
            Double similarity = itemSimilaritiesWithUser.getOrDefault(userItem, 0.0);

            numerator += userItemScore * similarity;
            denominator += Math.abs(similarity);
        }

        if (denominator == 0.0) {
            return 0.0;
        }

        return numerator / denominator;
    }

    //////////////////////////////////////////


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
