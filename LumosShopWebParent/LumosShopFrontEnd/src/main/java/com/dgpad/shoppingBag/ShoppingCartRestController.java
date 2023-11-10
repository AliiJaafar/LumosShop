package com.dgpad.shoppingBag;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.dgpad.product.ProductRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.CustomerNotFoundException;
import com.lumosshop.common.exception.ShoppingBagException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingCartRestController {

    @Autowired
    private ShoppingBagService bagService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/cart/add/{productID}/{qty}")
    public String addingTheProductToTheBag(HttpServletRequest httpServletRequest,
                                           @PathVariable(name = "productID")Integer ProductID,
                                           @PathVariable(name = "qty")Integer qty) throws CustomerNotFoundException {
        try {
            Customer customer = isTheCustomerAuthenticate(httpServletRequest);
            Integer targetedQty = bagService.addProduct(ProductID, customer, qty);
            return "Your shopping bag now contains " + targetedQty + " units of this product.";
        } catch (CustomerNotFoundException e) {
            return "Adding this product to your shopping bag is only possible for logged-in users.";
        } catch (ShoppingBagException e) {
            return e.getMessage();
        }
    }

    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        if (email == null) {
            throw new CustomerNotFoundException("Visitor");
        }
        return customerService.getCustomerByEmail(email);
    }
    @PostMapping("/cart/change/{productID}/{qty}")
    public String changingTheQtyProductInTheBag(HttpServletRequest httpServletRequest,
                                           @PathVariable(name = "productID")Integer ProductID,
                                           @PathVariable(name = "qty")Integer qty) throws CustomerNotFoundException {
        try {
            Customer customer = isTheCustomerAuthenticate(httpServletRequest);
            float eachProductTotal = bagService.updateQuantity(ProductID, qty, customer);
            return String.valueOf(eachProductTotal);
        } catch (CustomerNotFoundException e) {
            return "Changing The qty of products to your shopping bag is only possible for logged-in users.";

        }
    }

    @DeleteMapping("/bag/delete/{productId}")
    public String removeProduct(HttpServletRequest httpServletRequest , @PathVariable("productId") Integer productId) throws CustomerNotFoundException {
        try {
            Customer customer = isTheCustomerAuthenticate(httpServletRequest);
            bagService.deleteProductFromBag(customer, productId);
            Product product = productRepository.findById(productId).get();
            String productName = product.getName();
            return "The " + productName + " not longer exist in the bag";
        } catch (CustomerNotFoundException e) {
            return "deleting Any product is only possible for logged-in users.";
        }
    }

}

