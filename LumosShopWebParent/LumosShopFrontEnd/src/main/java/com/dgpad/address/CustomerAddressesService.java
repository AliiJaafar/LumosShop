package com.dgpad.address;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerAddressesService {
    @Autowired
    private CustomerAddressesRepository customerAddressesRepository;

    public CustomerAddresses retrievePrimaryAddress(Customer customer) {
        return customerAddressesRepository.findPrimaryByCustomerId(customer.getId());
    }
    public void setPrimary(Integer primaryId, Integer customerId) {
        if (primaryId != 0) {
            customerAddressesRepository.UpdateCustomerAddressesToPrimary(primaryId);
        }

        customerAddressesRepository.AssignRemainingToSecondary(primaryId, customerId);
    }


    public List<CustomerAddresses> displayAllAddresses(Customer customer) {
        return customerAddressesRepository.findByCustomer(customer);
    }

    public void save(CustomerAddresses address) {
        customerAddressesRepository.save(address);
    }

    public CustomerAddresses retrieveAddress(Integer customerId, Integer addressId) {
        return customerAddressesRepository.findByIdAndCustomer(customerId, addressId);
    }

    public void delete(Integer addressId, Integer customerId) {
        customerAddressesRepository.deleteByCustomerAndId(addressId, customerId);
    }


}
