package com.dgpad.order;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.exception.CustomerNotFoundException;
import com.lumosshop.common.exception.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;


    private Customer isTheCustomerAuthenticate(HttpServletRequest httpServletRequest) throws CustomerNotFoundException {
        String email = Utility.fetchCustomerEmailFromAuthSource(httpServletRequest);
        if (email == null) {
            throw new CustomerNotFoundException("Visitor");
        }
        return customerService.getCustomerByEmail(email);
    }
    @PostMapping("order/reverted")
    public ResponseEntity<?> OrderRevertedProcess(HttpServletRequest httpServletRequest, @RequestBody Request_Reverted requestReverted) throws CustomerNotFoundException {
        Customer customer = isTheCustomerAuthenticate(httpServletRequest);

        try {
            orderService.setOrderCUSTOMER_REQUESTED_RETURN(customer, requestReverted);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new Response_Reverted(requestReverted.getOrderID()), HttpStatus.OK);

    }
}
