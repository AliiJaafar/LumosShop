package com.dgpad.interactions.click;


import com.dgpad.customer.CustomerRepository;
import com.dgpad.product.ProductRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.interactions.Click;
import com.lumosshop.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClickService {

    @Autowired
    private ClickRepository clickRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;


    public void saveClick(Integer customerId,
                          Integer productId) {
        Customer customer = customerRepository.findById(customerId).get();
        Product product = productRepository.findById(productId).get();
        Click click = new Click();
        click.setCustomer(customer);
        click.setProduct(product);
        click.setTimestamp(new Date());
        clickRepository.save(click);
    }
}
