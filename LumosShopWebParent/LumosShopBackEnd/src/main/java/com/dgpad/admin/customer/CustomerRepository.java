package com.dgpad.admin.customer;

import com.lumosshop.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer,Integer>, CrudRepository<Customer, Integer> {

    @Query("select c from Customer c where c.email = ?1")
    public Customer findByEmail(String email);

    @Query("select c from Customer c where c.verificationCode = ?1")
    public Customer findByVerificationCode(String code);
    @Modifying
    @Query("update Customer c set c.enabled = ?2 where c.id = ?1")
    public void updateEnableStatus(Integer id, boolean status);

    @Query("select c from Customer c where concat(c.id,' ',c.firstName,' ',c.lastName,' ',c.email,' ',c.nation.name,' ' ,c.city,' ')like %?1%")
    public Page<Customer> findAll(String keyWord, Pageable pageable);

    public Long countById(Integer id);
}
