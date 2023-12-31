package com.dgpad.recommender;

import java.util.HashMap;
import java.util.Map;

public class ItemSimilarityCalculator {
    public static Map<Integer, Map<Integer, Double>> calculateItemSimilarities(Map<Integer, Map<Integer, Double>> userItemMatrix) {
        Map<Integer, Map<Integer, Double>> itemSimilarities = new HashMap<>();

        // Transpose the userItemMatrix to get item-user matrix
        Map<Integer, Map<Integer, Double>> itemUserMatrix = transposeMatrix(userItemMatrix);

        for (Map.Entry<Integer, Map<Integer, Double>> entry1 : itemUserMatrix.entrySet()) {
            Integer itemId1 = entry1.getKey();
            Map<Integer, Double> itemVector1 = entry1.getValue();

            for (Map.Entry<Integer, Map<Integer, Double>> entry2 : itemUserMatrix.entrySet()) {
                Integer itemId2 = entry2.getKey();
                Map<Integer, Double> itemVector2 = entry2.getValue();

                // Calculate cosine similarity between item vectors
                double similarity = calculateCosineSimilarity(itemVector1, itemVector2);

                // Store the similarity in the itemSimilarities matrix
                itemSimilarities
                        .computeIfAbsent(itemId1, k -> new HashMap<>())
                        .put(itemId2, similarity);
            }
        }

        return itemSimilarities;
    }
    private static Map<Integer, Map<Integer, Double>> transposeMatrix(Map<Integer, Map<Integer, Double>> matrix) {
        Map<Integer, Map<Integer, Double>> transposedMatrix = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, Double>> entry : matrix.entrySet()) {
            Integer rowKey = entry.getKey();
            Map<Integer, Double> row = entry.getValue();

            for (Map.Entry<Integer, Double> element : row.entrySet()) {
                Integer colKey = element.getKey();
                Double value = element.getValue();

                transposedMatrix
                        .computeIfAbsent(colKey, k -> new HashMap<>())
                        .put(rowKey, value);
            }
        }

        return transposedMatrix;
    }

    private static double calculateCosineSimilarity(Map<Integer, Double> vector1, Map<Integer, Double> vector2) {
        // Compute the dot product of the two vectors
        double dotProduct = 0.0;

        for (Map.Entry<Integer, Double> entry : vector1.entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();

            if (vector2.containsKey(key)) {
                dotProduct += value * vector2.get(key);
            }
        }

        // Compute the magnitudes of the two vectors
        double magnitude1 = calculateMagnitude(vector1);
        double magnitude2 = calculateMagnitude(vector2);

        // Handle special cases where vectors are empty or have different lengths
        if (magnitude1 == 0.0 || magnitude2 == 0.0) {
            return 0.0; // Return 0 for empty or invalid vectors
        }

        // Compute the cosine similarity
        double similarity = dotProduct / (magnitude1 * magnitude2);

        return similarity;
    }

    private static double calculateMagnitude(Map<Integer, Double> vector) {
        double sumOfSquares = 0.0;

        for (double value : vector.values()) {
            sumOfSquares += value * value;
        }

        return Math.sqrt(sumOfSquares);
    }

}
