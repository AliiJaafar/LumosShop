package com.dgpad.order;

import com.dgpad.Utility;
import com.dgpad.customer.CustomerService;
import com.lumosshop.common.entity.Currency;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.exception.CustomerNotFoundException;
import com.lumosshop.common.exception.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

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
    @GetMapping("/orders")
    public String displayTheFirstPage(HttpServletRequest httpServletRequest,
                                      Model model) throws CustomerNotFoundException {
        return displayingOrders(httpServletRequest, "id", "desc", 1, null, model);
    }

    @GetMapping("/orders/page/{pageNumber}")
    public String displayingOrders(HttpServletRequest httpServletRequest,
                                   String sortField,
                                   String sortDirection,
                                   @PathVariable(name = "pageNumber") int pageNumber,
                                   String keyword,
                                   Model model) throws CustomerNotFoundException {

        Customer customer = isTheCustomerAuthenticate(httpServletRequest);
        Page<Order> orderPage = orderService.displayAllOrderByPage(pageNumber, sortField, sortDirection, keyword, customer);
        List<Order> orderList = orderPage.getContent();

        long startCount = (long) (pageNumber - 1) * OrderService.NUMBER_OF_ORDER_IN_PAGE + 1;
        long endCount = startCount + OrderService.NUMBER_OF_ORDER_IN_PAGE - 1;
        if (endCount > orderPage.getTotalElements()) {
            endCount = orderPage.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";


        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElement", orderPage.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("orderList", orderList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/orders");
        model.addAttribute("pageTitle", "Orders");

        return "orders/customer-order-list";
//        return "orders/test-er";
    }

    @GetMapping("/orders/progress/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {

        try {
            Order order = orderService.findOrderById(id);

            Customer customer = isTheCustomerAuthenticate(httpServletRequest);

            model.addAttribute("order", order);
            model.addAttribute("customer", customer);
            model.addAttribute("title", "Orders specifics");

            return "orders/order-progress-modal";
        } catch (OrderNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/orders";
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Visitor Not Allowed Here");
            return "redirect:/orders";

        }

    }
}
