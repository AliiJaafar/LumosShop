package com.dgpad.checkout;

import com.dgpad.ShippingCharge.ShippingFeeService;
import com.dgpad.Utility;
import com.dgpad.address.CustomerAddressesService;
import com.dgpad.controlCenter.ControlService;
import com.dgpad.customer.CustomerService;
import com.dgpad.order.OrderService;
import com.dgpad.shoppingBag.ShoppingBagService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import com.lumosshop.common.entity.Shipping;
import com.lumosshop.common.entity.ShoppingBag;
import com.lumosshop.common.entity.control.MailCenter;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.order.Payment_Choice;
import com.lumosshop.common.exception.CustomerNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private CheckoutServiceLayer checkoutService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShippingFeeService shippingFeeService;

    @Autowired
    private ShoppingBagService shoppingBagService;

    @Autowired
    private CustomerAddressesService customerAddressesService;

    @Autowired
    private ControlService controlService;

    @Autowired
    private OrderService orderService;


    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        if (email == null) {
            throw new CustomerNotFoundException("Visitor");
        }
        return customerService.getCustomerByEmail(email);
    }
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest httpServletRequest) throws CustomerNotFoundException, MessagingException, UnsupportedEncodingException {


        Customer customer = isTheCustomerAuthenticate(httpServletRequest);


        CustomerAddresses primaryAddress = customerAddressesService.retrievePrimaryAddress(customer);

        Shipping fee = null;

        if (primaryAddress != null) {
            model.addAttribute("shippingAddress", primaryAddress.toString());
            fee = shippingFeeService.lookForShippingChargeWithAddress(primaryAddress);
        } else {
            model.addAttribute("shippingAddress", customer.retrieveTheFullAddress());
            fee = shippingFeeService.lookForShippingChargeForCustomer(customer);
        }

        if (fee == null) {
            return "redirect:/bag";
        }

        List<ShoppingBag> bagStocks = shoppingBagService.bagList(customer);
        CheckoutModel checkoutModel = checkoutService.SettingUp(bagStocks, fee);

       /* // Paypal
        String currencyCode = settingService.getCurrencyCode();
        PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
        String paypalClientId = paymentSettings.getClientID();*/

        model.addAttribute("customer", customer);

        model.addAttribute("checkoutModel", checkoutModel);
        model.addAttribute("bagStocks", bagStocks);



        return "Bag/checkout/checkout";
    }

    @PostMapping("/make_purchase")
    public String Make_a_purchase(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {

        String paymentChoice = httpServletRequest.getParameter("paymentMethod");

        Payment_Choice payment_choice = Payment_Choice.valueOf(paymentChoice);
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        CustomerAddresses primaryAddress = customerAddressesService.retrievePrimaryAddress(customer);
        Shipping fee = null;
        if (primaryAddress != null) {
            fee = shippingFeeService.lookForShippingChargeWithAddress(primaryAddress);
        } else {
            fee = shippingFeeService.lookForShippingChargeForCustomer(customer);
        }

        List<ShoppingBag> bagStocks = shoppingBagService.bagList(customer);
        CheckoutModel checkoutModel = checkoutService.SettingUp(bagStocks, fee);

        Order GeneratedOrder = orderService.newOrder(payment_choice, customer, bagStocks, primaryAddress, checkoutModel);

        shoppingBagService.deleteByTheCustomer(customer);

        try {

            MailCenter mailCenter = controlService.RetrieveTheControlsOfMailSystem();
            JavaMailSenderImpl mailSender = Utility.prepareMailSender(mailCenter);

            String Subject = mailCenter.retrieveTheOrderAcknowledgeTitle();
            String Message = mailCenter.retrieveTheOrderAcknowledgeContent();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setFrom(mailCenter.retrieveFromMail(),mailCenter.retrieveWhoTheSender());
            messageHelper.setTo(customer.getEmail());



            Subject = Subject.replace("[(ORDER_ID)]", String.valueOf(GeneratedOrder.getId()));
            Message = Message.replace("[(Order Number)]", String.valueOf(GeneratedOrder.getId()));
            Message = Message.replace("[(Customer Name)]", customer.getFullName());
            Message = Message.replace("[(Order Date)]", String.valueOf(GeneratedOrder.getOrderDate()));
            Message = Message.replace("[(Order Total)]", String.valueOf(GeneratedOrder.getTotalPrice()));
            Message = Message.replace("[(Delivery Address)]", GeneratedOrder.retrieveTheFullAddress());

            messageHelper.setSubject(Subject);

            messageHelper.setText(Message, true);
            mailSender.send(mimeMessage);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }

        return "Bag/checkout/PurchaseClosed";

    }



}
