package com.dgpad.security;

import com.dgpad.customer.CustomerService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Identification_method;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SystemLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        CustomerDetails customerDetails = (CustomerDetails) authentication.getPrincipal();

        Customer customer = customerDetails.fetchTheCustomer();

        customerService.changeIdentification(customer, Identification_method.SYSTEM);

        super.onAuthenticationSuccess(request, response, authentication);
    }

}

