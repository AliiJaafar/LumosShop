package com.dgpad.admin.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/order_followup/change/{id}/{phase}")
    public OrderDTO changeOrderPhase(@PathVariable("id") Integer orderID,
                                     @PathVariable("phase") String phase) {
        orderService.changeOrderPhase(orderID, phase);
        return new OrderDTO(orderID, phase);
    }
}
