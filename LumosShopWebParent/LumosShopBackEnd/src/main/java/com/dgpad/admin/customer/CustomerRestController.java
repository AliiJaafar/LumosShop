package com.dgpad.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer/checkEmail")
    public String checkEmail(Integer id , @Param("email") String email) {
        return customerService.isEmailUnique(id,email) ? "OK" : "Duplicated";
    }
}
