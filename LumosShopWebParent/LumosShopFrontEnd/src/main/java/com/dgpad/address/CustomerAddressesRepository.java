package com.dgpad.address;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerAddressesRepository extends CrudRepository<CustomerAddresses, Integer> {


    public List<CustomerAddresses> findByCustomer(Customer customer);

    @Query("select ad from CustomerAddresses ad where ad.customer.id = ?1 and ad.id = ?2")
    public CustomerAddresses findByIdAndCustomer(Integer customerId, Integer CustomerAddressesId);

    @Modifying
    @Query("delete from CustomerAddresses ad where ad.customer.id = ?1 and ad.id = ?2")
    public void deleteByCustomerAndId(Integer addressId, Integer customerId);


    @Modifying
    @Query("update CustomerAddresses ad set ad.isPrimary = true where ad.id = ?1")
    public void UpdateCustomerAddressesToPrimary(Integer id);

    @Modifying
    @Query("update CustomerAddresses ad set ad.isPrimary = false where ad.id != ?1 and ad.customer.id = ?2")
    public void AssignRemainingToSecondary(Integer primaryId, Integer customerId);

    @Query("select ad from CustomerAddresses ad where ad.isPrimary = true and ad.customer.id = ?1")
    public CustomerAddresses findPrimaryByCustomerId(Integer customerId);
}
