package com.dgpad.customer;

import com.dgpad.Utility;
import com.dgpad.controlCenter.ControlService;
import com.dgpad.security.CustomerDetails;
import com.dgpad.security.oauth.ClientOAuth2User;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.control.MailCenter;
import com.lumosshop.common.entity.control.Nation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ControlService controlService;

    @GetMapping("/register")
    public String RegistrationForm(Model model) {
        List<Nation> nationList = customerService.nationList();
        model.addAttribute("title", "Customer - Register");

        model.addAttribute("nationList", nationList);
        model.addAttribute("customer", new Customer());

        return "customer/register-form";
    }

    @PostMapping("/setUp-customer")
    public String enrollCustomer(Customer customer,
                                 Model model,
                                 HttpServletRequest httpServletRequest) throws MessagingException, UnsupportedEncodingException {


        model.addAttribute("title", "Sign-up successful");
        customerService.registerNewCustomer(customer);
        forwardVerificationMessage(httpServletRequest, customer);

        model.addAttribute("name", customer.getFullName());

        return "customer/AccountCreated";

    }

    private void forwardVerificationMessage(HttpServletRequest httpServletRequest, Customer customer) throws MessagingException, UnsupportedEncodingException {
        MailCenter mailCenter = controlService.RetrieveTheControlsOfMailSystem();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(mailCenter);

        String To = customer.getEmail();
        String Subject = mailCenter.retrieveTheVerificationTitle();
        String Message = mailCenter.retrieveTheVerificationMessageContent();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        messageHelper.setFrom(mailCenter.retrieveFromMail(), mailCenter.retrieveWhoTheSender());
        messageHelper.setTo(To);
        messageHelper.setSubject(Subject);
        String VerificationLink = Utility.getAddress(httpServletRequest) + "/verify?code=" + customer.getVerificationCode();

        Message = Message.replace("((Replace_USER))", customer.getFullName());
        Message = Message.replace("((Replace_URL))", VerificationLink);

        messageHelper.setText(Message, true);
        mailSender.send(mimeMessage);


    }

    @GetMapping("/my-account")
    public String displayCustomerAccount(HttpServletRequest httpServletRequest, Model model) {

        model.addAttribute("pageTitle", "Account");

        List<Nation> nationList = customerService.nationList();
        model.addAttribute("nationList", nationList);

        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        Customer customer = customerService.getCustomerByEmail(email);
        model.addAttribute("customer", customer);

        //OAuth2AuthenticationToken
        //UsernamePasswordAuthenticationToken
        //RememberMeAuthenticationToken

        return "customer/account-Info";
    }




    @GetMapping("/verify")
    public String VerificationProtocol(Model model, @Param("code") String code) {

        boolean isVerify = customerService.verify(code);
        return "customer/" + (isVerify ? "Identity-Checked" : "Unchecked");
    }

    @PostMapping("/alter-Account")
    public String alterAccount(Model model, RedirectAttributes redirectAttributes, Customer customer,
                               HttpServletRequest httpServletRequest) {


        customerService.alter(customer);
        redirectAttributes.addFlashAttribute("message", "We've updated Your Account successfully");

        String redirectOp = httpServletRequest.getParameter("redirect");
        String redirectURL = "redirect:/my-account";

        if ("addresses".equals(redirectOp)) {
            redirectURL = "redirect:/addresses";
        }
        if ("bag".equals(redirectOp)) {
            redirectURL = "redirect:/bag";
        }
        return redirectURL;
    }

}
