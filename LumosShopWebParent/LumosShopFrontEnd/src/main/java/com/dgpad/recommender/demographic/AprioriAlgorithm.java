package com.dgpad.recommender.demographic;

import com.lumosshop.common.entity.ItemSet;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.order.Order_Summary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Component
public class AprioriAlgorithm {

    public static List<ItemSet> generateCandidateItemSets(List<Order> orders, int minSupport) {
        List<ItemSet> candidateItemSets = new ArrayList<>();

        // Step 1: Find frequent 1-itemsets
        List<ItemSet> frequent1ItemSets = findFrequent1ItemSets(orders, minSupport);
        candidateItemSets.addAll(frequent1ItemSets);

        // Step 2: Generate candidate itemsets of size k > 1
        int k = 2;
        while (!frequent1ItemSets.isEmpty()) {
            List<ItemSet> candidateSets = generateCandidates(frequent1ItemSets, k);
            List<ItemSet> frequentKItemSets = pruneInfrequentItemSets(candidateSets, orders, minSupport);
            candidateItemSets.addAll(frequentKItemSets);
            frequent1ItemSets = frequentKItemSets;
            k++;
        }

        return candidateItemSets;
    }

    private static List<ItemSet> findFrequent1ItemSets(List<Order> orders, int minSupport) {
        List<ItemSet> frequent1ItemSets = new ArrayList<>();
        Set<String> allProducts = new HashSet<>();

        // Collect all unique products
        for (Order order : orders) {
            for (Order_Summary orderSummary : order.getOrderSummaries()) {
                allProducts.add(orderSummary.getProduct().getName());
            }
        }

        // Count occurrences of each product
        for (String product : allProducts) {
            int itemCount = 0;
            for (Order order : orders) {
                for (Order_Summary orderSummary : order.getOrderSummaries()) {
                    if (orderSummary.getProduct().getName().equals(product)) {
                        itemCount++;
                    }
                }
            }

            if (itemCount >= minSupport) {
                ItemSet itemSet = new ItemSet();
                itemSet.addItem(product);
                frequent1ItemSets.add(itemSet);
            }
        }

        return frequent1ItemSets;
    }

    private static List<ItemSet> generateCandidates(List<ItemSet> frequentItemSets, int k) {
        List<ItemSet> candidateItemSets = new ArrayList<>();
        int size = frequentItemSets.size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                ItemSet itemSet1 = frequentItemSets.get(i);
                ItemSet itemSet2 = frequentItemSets.get(j);

                if (itemSet1.getItems().subList(0, k - 2).equals(itemSet2.getItems().subList(0, k - 2))) {
                    ItemSet candidateSet = new ItemSet();
                    Set<String> items = new HashSet<>(itemSet1.getItems());
                    items.addAll(itemSet2.getItems());
                    candidateSet.setItems(new ArrayList<>(items));

                    if (!hasInfrequentSubset(candidateSet, frequentItemSets, k - 1)) {
                        candidateItemSets.add(candidateSet);
                    }
                }
            }
        }

        return candidateItemSets;
    }

    private static boolean hasInfrequentSubset(ItemSet candidateSet, List<ItemSet> frequentItemSets, int k) {
        List<ItemSet> subsets = generateSubsets(candidateSet, k);

        for (ItemSet subset : subsets) {
            if (!frequentItemSets.contains(subset)) {
                return true;
            }
        }
        return false;
    }

    private static List<ItemSet> generateSubsets(ItemSet itemSet, int k) {
        List<ItemSet> subsets = new ArrayList<>();
        generateSubsets(itemSet, new ItemSet(), 0, 0, k, subsets);
        return subsets;
    }

    private static void generateSubsets(ItemSet itemSet, ItemSet currentSubset, int index, int currentSize, int k, List<ItemSet> subsets) {
        if (currentSize == k) {
            subsets.add(new ItemSet(currentSubset.getItems()));
            return;
        }

        for (int i = index; i < itemSet.getItems().size(); i++) {
            currentSubset.addItem(itemSet.getItems().get(i));
            generateSubsets(itemSet, currentSubset, i + 1, currentSize + 1, k, subsets);
            currentSubset.getItems().remove(currentSubset.getItems().size() - 1);
        }
    }

    private static List<ItemSet> pruneInfrequentItemSets(List<ItemSet> candidateItemSets, List<Order> transactions, int minSupport) {
        List<ItemSet> frequentItemSets = new ArrayList<>();
        for (ItemSet candidateSet : candidateItemSets) {
            int count = countSupport(candidateSet, transactions);
            if (count >= minSupport) {
                frequentItemSets.add(candidateSet);
            }
        }
        return frequentItemSets;
    }

    static int countSupport(ItemSet itemSet, List<Order> orders) {
        int count = 0;
        for (Order order : orders) {
            boolean containsAll = order.getOrderSummaries()
                    .stream()
                    .map(summary -> summary.getProduct().getName())
                    .allMatch(itemSet.getItems()::contains);

            if (containsAll) {
                count++;
            }
        }
        return count;
    }



}
