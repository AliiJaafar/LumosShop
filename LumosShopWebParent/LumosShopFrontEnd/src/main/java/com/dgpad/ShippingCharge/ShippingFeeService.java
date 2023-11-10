package com.dgpad.ShippingCharge;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import com.lumosshop.common.entity.Shipping;
import com.lumosshop.common.entity.control.Nation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingFeeService {
    @Autowired
    private ShippingFeeRepository feeRepository;

    public Shipping lookForShippingChargeForCustomer(Customer customer) {
        String city = customer.getCity();

        if (city == null || city.isEmpty()) {
            return feeRepository.findByNation(customer.getNation());
        }

        return feeRepository.findByNationAndCity(customer.getNation(), city);

    }
    public Shipping lookForShippingChargeWithAddress(CustomerAddresses address) {
        String city = address.getCity();

        if (city == null || city.isEmpty()) {
            return feeRepository.findByNation(address.getNation());
        }
        return feeRepository.findByNationAndCity(address.getNation(), city);

    }
}
