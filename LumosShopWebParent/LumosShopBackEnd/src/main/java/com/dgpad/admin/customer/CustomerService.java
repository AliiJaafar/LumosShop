package com.dgpad.admin.customer;

import com.dgpad.admin.control.NationRepository;
import com.dgpad.admin.user.UserNotFoundException;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Role;
import com.lumosshop.common.entity.User;
import com.lumosshop.common.entity.control.Nation;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional

public class CustomerService {
    public static final int CUSTOMER_PER_PAGE = 8;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public List<Nation> nationList() {
        return nationRepository.findAllByOrderByNameAsc();
    }
    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public List<Customer> findAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    public Page<Customer> listCustomerByPage(int pageNumber,
                                             String sortField,
                                             String sortDirection,
                                             String Keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, CUSTOMER_PER_PAGE, sort);

        if (Keyword != null) {
            return customerRepository.findAll(Keyword, pageable);
        }
        return customerRepository.findAll(pageable);
    }

    public Customer save(Customer customer) {
        boolean isUpdatingCustomer = (customer.getId() != null);
        Customer existingCustomer = customerRepository.findById(customer.getId()).get();

        if (isUpdatingCustomer) {
            if (customer.getPassword().isEmpty()) {
                customer.setPassword(existingCustomer.getPassword());
            } else {
                encodePassword(customer);
            }
        } else {
            customer.setCreatedTime(new Date());
            encodePassword(customer);

        }

        customer.setRecoveryCode(existingCustomer.getRecoveryCode());
        customer.setIdentification_method(existingCustomer.getIdentification_method());
        customer.setVerificationCode(existingCustomer.getVerificationCode());
        customer.setEnabled(existingCustomer.isEnabled());
        return customerRepository.save(customer);
    }
    public Customer updateCustomerAccount(Customer CustomerForm) {
        Customer CustomerDb = customerRepository.findById(CustomerForm.getId()).get();

        if (!CustomerForm.getPassword().isEmpty()) {
            CustomerDb.setPassword(CustomerForm.getPassword());
            encodePassword(CustomerDb);
        }
        return customerRepository.save(CustomerDb);
    }
    public void deleteById(int id) throws CustomerNotFoundException {

        Long countById = customerRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CustomerNotFoundException("could not found this customer id - " + id);
        }
        customerRepository.deleteById(id);

    }
    public Customer findCustomerById(int id) throws CustomerNotFoundException {
        try {
            return customerRepository.findById(id).get();

        } catch (NoSuchElementException e) {
            throw new CustomerNotFoundException("Could not found this user id :  " + id);
        }

    }

    public void UpdateTheCustomerStatus(Integer id, boolean status) {
        customerRepository.updateEnableStatus(id, status);
    }

    public boolean isEmailUnique(Integer id, String email) {

        Customer customerByEmail = customerRepository.findByEmail(email);

        if (customerByEmail == null) return true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            return false;
        } else {
            return customerByEmail.getId().equals(id);
        }

    }
}
