package com.dgpad.shoppingBag;

import com.dgpad.ShippingCharge.ShippingFeeService;
import com.dgpad.Utility;
import com.dgpad.address.CustomerAddressesService;
import com.dgpad.customer.CustomerService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import com.lumosshop.common.entity.Shipping;
import com.lumosshop.common.entity.ShoppingBag;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingBagController {
    @Autowired
    private ShoppingBagService bagService;
    @Autowired
    private ShippingFeeService shippingFeeService;
    @Autowired
    private CustomerAddressesService addressesService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/bag")
    public String displayTheBag(HttpServletRequest httpServletRequest,Model model) throws CustomerNotFoundException {
        model.addAttribute("pageTitle", "your bag");
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        List<ShoppingBag> bagList = bagService.bagList(customer);

        CustomerAddresses address = addressesService.retrievePrimaryAddress(customer);

        boolean CustomerUsingHisOriginalAddress = false;
        Shipping shipping = null;
        if (address != null) {
            shipping = shippingFeeService.lookForShippingChargeWithAddress(address);
        } else {
            CustomerUsingHisOriginalAddress = true;
            shipping = shippingFeeService.lookForShippingChargeForCustomer(customer);
        }

        boolean CoveredShippingArea = shipping != null;
        float TotalBagPrice = 0f;
        for (ShoppingBag product : bagList) {
            TotalBagPrice += product.getThePriceMultiplyByQty();
        }
        model.addAttribute("CoveredShippingArea", CoveredShippingArea);
        model.addAttribute("bagList", bagList);
        model.addAttribute("CustomerUsingHisOriginalAddress", CustomerUsingHisOriginalAddress);
        model.addAttribute("TotalBagPrice", TotalBagPrice);
        return "Bag/shopping-bag";
    }

    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        if (email == null) {
            throw new CustomerNotFoundException("Visitor");
        }
        return customerService.getCustomerByEmail(email);
    }

}
