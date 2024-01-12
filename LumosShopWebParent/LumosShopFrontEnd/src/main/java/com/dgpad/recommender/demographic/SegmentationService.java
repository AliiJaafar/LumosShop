package com.dgpad.recommender.demographic;

import com.dgpad.customer.CustomerRepository;
import com.dgpad.order.OrderSummaryRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.ItemSet;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Instances;

import java.util.List;

@Service
public class SegmentationService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    @Autowired
    private AprioriAlgorithm aprioriAlgorithm;

    public List<Customer> getCustomerInCity(String city) {
        return customerRepository.findByCity(city);
    }
    public List<Customer> getCustomerInCountry(String nation) {
        return customerRepository.findByNation(nation);
    }

    public List<Object[]> getPopularProducts() {
        return orderSummaryRepository.findPopularProducts();
    }



    public List<ItemSet> generateFrequentItemSets(List<Order> orders, int minSupport) {
        return AprioriAlgorithm.generateCandidateItemSets(orders, minSupport);
    }




}
