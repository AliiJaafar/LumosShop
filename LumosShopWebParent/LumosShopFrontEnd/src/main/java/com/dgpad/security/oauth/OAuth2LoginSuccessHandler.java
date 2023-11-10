package com.dgpad.security.oauth;

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
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CustomerService customerService;




    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        ClientOAuth2User clientOAuth2User = (ClientOAuth2User) authentication.getPrincipal();

        String name = clientOAuth2User.getName();
        String email = clientOAuth2User.getEmail();
        String code = request.getLocale().getCountry();
        String IP = request.getRemoteAddr();
        String local = clientOAuth2User.getLocal();
        String clientName = clientOAuth2User.getClientName();

        Customer customer = customerService.getCustomerByEmail(email);


        if (customer == null) {

            customerService.registerNewCustomerUsingOAuthAuthentication(name, email, code);

        } else {
            customerService.changeIdentification(customer, Identification_method.GOOGLE);

        }
        System.out.println("OAuth2LoginSuccessHandle.CLASS -- Name: " + name + "|email: " + email + "|code: " + code + "|IP Address: " + IP);
        System.out.println("Client Name: " + clientName+ "||");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
