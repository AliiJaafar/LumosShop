package com.dgpad.customer;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Identification_method;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("update Customer c set c.identification_method = ?2 where c.id = ?1")
    @Modifying
    public void updateIdentificationMethod(Integer ID , Identification_method identification_method);



    @Query("select c from Customer c where c.email = ?1")
    public Customer findByEmail(String email);

    @Query("select c from Customer c where c.verificationCode = ?1")
    public Customer findByVerificationCode(String code);
    @Query("select c from Customer c where c.RecoveryCode = ?1")
    public Customer findByRecoveryCode(String code);
    @Modifying
    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null where c.id = ?1")
    public void enable(Integer id);
}
