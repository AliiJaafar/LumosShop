package com.dgpad.recommender;

import weka.associations.Apriori;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;

public class AprioriService {
    public void runApriori(String filePath) throws Exception {
        // Load dataset
        Instances data = new Instances(new BufferedReader(new FileReader(filePath)));

        // Create Apriori instance
        Apriori apriori = new Apriori();

        // Set options if needed
        // apriori.setOptions(...);

        // Build model
        apriori.buildAssociations(data);

        // Print the generated rules
        System.out.println(apriori);

        // You can further customize the output or use the rules as needed
    }
}
