package com.dgpad;

import com.dgpad.security.oauth.ClientOAuth2User;
import com.lumosshop.common.entity.control.MailCenter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Properties;

public class Utility {

    public static String fetchCustomerEmailFromAuthSource(HttpServletRequest httpServletRequest) {
        Object authentication = httpServletRequest.getUserPrincipal();
        if (authentication == null) return null;
        String customerEmail = null;

        if (authentication instanceof UsernamePasswordAuthenticationToken ||
                authentication instanceof RememberMeAuthenticationToken) {
            customerEmail = httpServletRequest.getUserPrincipal().getName();
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2 = (OAuth2AuthenticationToken) authentication;
            ClientOAuth2User clientOAuth2User = (ClientOAuth2User) oAuth2.getPrincipal();
            customerEmail = clientOAuth2User.getEmail();
        }
        return customerEmail;
    }
    public static String getAddress(HttpServletRequest httpServletRequest) {
        String address = httpServletRequest.getRequestURL().toString();
        return address.replace(httpServletRequest.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(MailCenter mailCenter) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(mailCenter.retrieveTheUserName());
        mailSender.setPassword(mailCenter.retrieveThePassword());
        mailSender.setHost(mailCenter.retrieveTheHost());
        mailSender.setPort(mailCenter.retrieveThePortNum());
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", mailCenter.retrieveTheSMPT_Secured());
        properties.setProperty("mail.smtp.auth", mailCenter.retrieveTheSMPT_Auth());
        mailSender.setJavaMailProperties(properties);

        return mailSender;

    }

}
