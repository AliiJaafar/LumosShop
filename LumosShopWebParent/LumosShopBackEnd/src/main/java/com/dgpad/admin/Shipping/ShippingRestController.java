package com.dgpad.admin.Shipping;

import com.lumosshop.common.exception.ProductNotFoundException;
import com.lumosshop.common.exception.ShippingFeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingRestController {
    @Autowired
    private ShippingService shippingService;

    @PostMapping("/get_shipping_charge")
    public String getShippingCost(Integer productId, Integer nationID, String city)
            throws ShippingFeeNotFoundException, ProductNotFoundException {
        float shippingCharge = shippingService.determineShippingCharge(productId, nationID, city);

        return String.valueOf(shippingCharge);
    }






}
