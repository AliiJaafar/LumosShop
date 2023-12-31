package com.dgpad.interactions.click;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.dgpad.interactions.InteractionRepository;
import com.dgpad.product.ProductService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.interactions.Interaction;
import com.lumosshop.common.entity.interactions.InteractionType;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.CustomerNotFoundException;
import com.lumosshop.common.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ClickRestController {

    @Autowired
    private ClickService clickService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InteractionRepository interactionRepository;

    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        return customerService.getCustomerByEmail(email);
    }
    @PostMapping("/clicks/store/{productID}")
    public void recordClick(@PathVariable(name = "productID") Integer productID, HttpServletRequest httpServletRequest) throws CustomerNotFoundException, ProductNotFoundException {

        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        clickService.saveClick(customer.getId(), productID);
        Interaction interaction = new Interaction();
        interaction.setInteractionType(InteractionType.CLICK);
        interaction.setCustomer(customer);
        Product product = productService.retrieveProduct(productID);
        interaction.setProduct(product);
        interaction.setValue(1);
        interaction.setTimestamp(new Date());

        interactionRepository.saveOrIncrementValue(interaction);

    }


}
