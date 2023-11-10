package com.dgpad.customer;

import com.dgpad.controlCenter.NationRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Identification_method;
import com.lumosshop.common.entity.control.Nation;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NationRepository nationRepository;
    @Autowired
    private CustomerRepository customerRepository;


    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void registerNewCustomer(Customer customer) {
        encodePassword(customer);
        customer.setIdentification_method(Identification_method.SYSTEM);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        String randomString = UUID.randomUUID().toString().replace("-", "");
        customer.setVerificationCode(randomString);

        customerRepository.save(customer);


    }

    public boolean verify(String code) {
        Customer customer = customerRepository.findByVerificationCode(code);

        if (customer == null || customer.isEnabled()) {
            return false;
        } else {
            customerRepository.enable(customer.getId());
            return true;
        }
    }

    private void encodePassword(Customer customer) {
        String encryptedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encryptedPassword);

    }

    public void recoverPassword(String code, String UpdatedPassword) throws CustomerNotFoundException {

        Customer customer = customerRepository.findByRecoveryCode(code);

        if (customer == null) {
            throw new CustomerNotFoundException("No customer matches this code in our system; it's rejected.");
        }
        customer.setPassword(UpdatedPassword);
        encodePassword(customer);
        customerRepository.save(customer);

    }

    public List<Nation> nationList() {
        return nationRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailDuplicate(String email) {
        Customer customer = customerRepository.findByEmail(email);
        return customer != null;
    }

    public void changeIdentification(Customer customer, Identification_method method) {
        if (!customer.getIdentification_method().equals(method)) {
            customerRepository.updateIdentificationMethod(customer.getId(), method);
        }
    }

    public void registerNewCustomerUsingOAuthAuthentication(String name, String email, String code) {
        Customer customer = new Customer();

        setName(name, customer);
        customer.setEmail(email);
        customer.setEnabled(true);
        customer.setPassword("");
        customer.setCreatedTime(new Date());
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setIdentification_method(Identification_method.GOOGLE);
        customer.setPhoneNumber("");
        setTheNation(code, customer);

        customerRepository.save(customer);

    }

    private void setTheNation(String code, Customer customer) {

        Nation nationByCode = nationRepository.findByCode(code);
        if (nationByCode == null) {

            customer.setNation(null);
            System.out.println("CustomerService.CLASS (Nation to the new Google login customer not found!");


        } else {

            System.out.println("CustomerService.CLASS (Nation to the new Google login customer is found: " + nationByCode.getName());
            customer.setNation(nationByCode);
        }
    }

    public void alter(Customer customer) {

        Customer existingCustomer = customerRepository.findById(customer.getId()).get();

        if (existingCustomer.getIdentification_method().equals(Identification_method.SYSTEM)) {

            if (customer.getPassword().isEmpty()) {
                customer.setPassword(existingCustomer.getPassword());
            } else {
                String encodedPas = passwordEncoder.encode(customer.getPassword());
                customer.setPassword(encodedPas);
            }
        } else {
            customer.setPassword(existingCustomer.getPassword());
        }

        customer.setRecoveryCode(existingCustomer.getRecoveryCode());
        customer.setCreatedTime(existingCustomer.getCreatedTime());
        customer.setEnabled(existingCustomer.isEnabled());
        customer.setVerificationCode(existingCustomer.getVerificationCode());
        customer.setIdentification_method(existingCustomer.getIdentification_method());


        customerRepository.save(customer);
    }

    private void setName(String name, Customer customer) {
        String[] nameParts = name.split(" ");

        if (nameParts.length > 0) {
            customer.setFirstName(nameParts[0]);
        } else {
            customer.setFirstName("");
        }

        if (nameParts.length > 1) {
            String lastName = name.substring(nameParts[0].length()).trim();
            customer.setLastName(lastName);
        } else {
            customer.setLastName("");
        }
    }


    public String changeRecoveryCode(String email) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByEmail(email);

        if (customer != null) {
            String code = UUID.randomUUID().toString().replace("-", "");
            customer.setRecoveryCode(code);
            customerRepository.save(customer);

            return code;
        } else {
            throw new CustomerNotFoundException("We've not Fount Any Customer with this email");
        }
    }

    public Customer retrieveByRecoveryPassword(String code) {
       return customerRepository.findByRecoveryCode(code);
    }
}
