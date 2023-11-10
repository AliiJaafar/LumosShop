package com.dgpad.customer;

import com.dgpad.Utility;
import com.dgpad.controlCenter.ControlService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.control.MailCenter;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class passwordController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ControlService controlService;


    @PostMapping("/forgot-password")
    public String ManageRequestForForgotPassword(Model model, HttpServletRequest httpServletRequest,Customer customer) throws CustomerNotFoundException {

        String email = httpServletRequest.getParameter("emailF");
        try {
            String code = customerService.changeRecoveryCode(email);
            forwardTheMessageThrowEmail(email, code, httpServletRequest);
            model.addAttribute("message", "We've send an recovery code to your email");
            customerService.changeRecoveryCode(email);
        } catch (CustomerNotFoundException e) {
            model.addAttribute("exception", e.getMessage());
        } catch (MessagingException | UnsupportedEncodingException exception) {
            model.addAttribute("exception", "We've couldn't send the email. Something went wrong.");
        }
        return "login";
    }

    private void forwardTheMessageThrowEmail(String email, String code, HttpServletRequest httpServletRequest) throws MessagingException, UnsupportedEncodingException {
        Customer customer = customerService.getCustomerByEmail(email);
        MailCenter mailCenter = controlService.RetrieveTheControlsOfMailSystem();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(mailCenter);

        String Subject = mailCenter.retrieveTheRecoveryPasswordTitle();
        String Message = mailCenter.retrieveTheRecoveryPasswordMessageContent();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

        messageHelper.setFrom(mailCenter.retrieveFromMail(),mailCenter.retrieveWhoTheSender());
        messageHelper.setTo(email);
        messageHelper.setSubject(Subject);

        String ResetLink = Utility.getAddress(httpServletRequest) + "/RecoverPassword?code=" + code;

        Message = Message.replace("[Name]", customer.getFullName());
        Message = Message.replace("[Reset_Link]", ResetLink);

        messageHelper.setText(Message, true);
        mailSender.send(mimeMessage);

    }
    @GetMapping("/RecoverPassword")
    public String RecoverPasswordProtocol(@Param("code") String code,Model model) {

        Customer customer = customerService.retrieveByRecoveryPassword(code);
        if (customer != null) {
            model.addAttribute("code", code);
        } else {
            model.addAttribute("error", "Rejected Recovery Code");
            return "customer/recover-password-process";
        }
        return "customer/recover-password-form";
    }

    @PostMapping("/recover-password")
    public String handleRecovering(Model model, HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String code = httpServletRequest.getParameter("code");
        String updatedPassword = httpServletRequest.getParameter("Password");

        try {
            customerService.recoverPassword(code, updatedPassword);
            model.addAttribute("message", "Your password got a refresh – it's all-new now!");

        } catch (CustomerNotFoundException exception) {
            model.addAttribute("error", exception.getMessage());
        }

        return "customer/recover-password-process";
    }
}
