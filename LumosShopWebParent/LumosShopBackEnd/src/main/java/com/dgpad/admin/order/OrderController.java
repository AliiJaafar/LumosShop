package com.dgpad.admin.order;

import com.dgpad.admin.control.ControlService;
import com.dgpad.admin.customer.CustomerService;
import com.lumosshop.common.entity.control.Control;
import com.lumosshop.common.entity.control.Nation;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.order.OrderFollowUp;
import com.lumosshop.common.entity.order.Order_Phase;
import com.lumosshop.common.entity.order.Order_Summary;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ControlService controlService;
    @Autowired
    private CustomerService customerService;


    private void FetchingAllCurrencyControl(HttpServletRequest httpServletRequest) {

        List<Control> controls = controlService.getAllCurrencySystems();

        for (Control c : controls) {

            httpServletRequest.setAttribute(c.getKey(), c.getValue());
            System.out.println(c.getKey() + "||" + c.getValue());
        }
    }

    @GetMapping("/orders")
    public String listOrdersForFirstPage(Model model, HttpServletRequest httpServletRequest) {
        return listByPage(model, 1, "id", "asc", null, httpServletRequest);
    }

    @GetMapping("/orders/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable(name = "pageNumber") int pageNumber,
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection,
                             @Param("keyword") String keyword,
                             HttpServletRequest httpServletRequest) {

        Page<Order> page = orderService.listOrderByPage(pageNumber, sortField, sortDirection, keyword);
        List<Order> orderList = page.getContent();

        long startCount = (long) (pageNumber - 1) * OrderService.ORDER_PER_PAGE + 1;
        long endCount = startCount + OrderService.ORDER_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";


        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("orderList", orderList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/orders");
        model.addAttribute("pageTitle", "Orders");

        FetchingAllCurrencyControl(httpServletRequest);

        return "order/orders";
    }

    @GetMapping(value = "/orders/add")
    public String showOrderForm(Model model) {

        Order order = new Order();

        model.addAttribute("nationList", customerService.nationList());

        model.addAttribute("order", order);

        model.addAttribute("pageTitle", "Add New Order");
        model.addAttribute("title", "Order Form - NEW");

        return "order/order-form";
    }

    @GetMapping("/order/edit/{id}")
    public String edit(@PathVariable(name = "id") int theId,
                       Model theModel, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        try {
            Order order = orderService.findOrderById(theId);
            FetchingAllCurrencyControl(httpServletRequest);

            List<Nation> nationList = customerService.nationList();
            theModel.addAttribute("nationList", nationList);
            theModel.addAttribute("order", order);
            theModel.addAttribute("pageTitle", "Edit Order ID : ( " + theId + " )");

            return "order/order-form";
        } catch (OrderNotFoundException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());

            return "redirect:/orders";
        }
    }

    @GetMapping("/orders/info/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {

        try {
            Order order = orderService.findOrderById(id);

            FetchingAllCurrencyControl(httpServletRequest);

            model.addAttribute("order", order);
            model.addAttribute("title", "Orders specifics");

            return "order/order_specifics";
        } catch (OrderNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/orders";
        }

    }

    @GetMapping("/orders/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws OrderNotFoundException {
        try {
            orderService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Order ID " + id + " has been deleted from the system with success.");
        } catch (OrderNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        // redirect to
        return "redirect:/orders";
    }

    @PostMapping("/order/saving")
    public String saveTheOrder(HttpServletRequest httpServletRequest, Order order, RedirectAttributes redirectAttributes) {
        String nation = httpServletRequest.getParameter("nationName");
        order.setNation(nation);

        updateOrderStamps(order, httpServletRequest);
        uploadStocksDetails(httpServletRequest, order);
        orderService.save(order);
        redirectAttributes.addFlashAttribute("message", "The modification of order ID " + order.getId() + " has been executed successfully.");

        return "redirect:/orders";
    }

    public void uploadStocksDetails(HttpServletRequest httpServletRequest, Order order) {
        String[] productIDs = httpServletRequest.getParameterValues("productID");
        String[] summaryIDs = httpServletRequest.getParameterValues("summaryID");
        String[] Qts = httpServletRequest.getParameterValues("quantity");
        String[] productPrices = httpServletRequest.getParameterValues("productPrice");
        String[] productIntermediateSum = httpServletRequest.getParameterValues("productInterSum");
        String[] productShippingCharge = httpServletRequest.getParameterValues("productShippingCharge");
        String[] productSummaryCosts = httpServletRequest.getParameterValues("productSummaryCost");

        Set<Order_Summary> orderSummaries = order.getOrderSummaries();
        for (int i = 0; i < summaryIDs.length; i++) {


            Order_Summary orderSummary = new Order_Summary();
            Integer summaryID = Integer.parseInt(summaryIDs[i]);

            if (summaryID > 0) {
                orderSummary.setId(summaryID);
            }

            orderSummary.setOrder(order);
            orderSummary.setProduct(new Product(Integer.parseInt(productIDs[i])));
            orderSummary.setProductCost(Float.parseFloat(productSummaryCosts[i]));
            orderSummary.setInterSum(Float.parseFloat(productIntermediateSum[i]));
            orderSummary.setShippingCharge(Float.parseFloat(productShippingCharge[i]));
            orderSummary.setQty(Integer.parseInt(Qts[i]));
            orderSummary.setItemPrice(Float.parseFloat(productPrices[i]));

            orderSummaries.add(orderSummary);

        }

    }

    public void updateOrderStamps(Order order, HttpServletRequest httpServletRequest) {

        String[] stampIDs = httpServletRequest.getParameterValues("stampId");
        String[] stampPhases = httpServletRequest.getParameterValues("stampPhase");
        String[] TimeStamp = httpServletRequest.getParameterValues("TimeStamp");
        String[] stampRemark = httpServletRequest.getParameterValues("stampRemark");

        List<OrderFollowUp> orderFollowUps = order.getOrderFollowUps();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        for (int i = 0; i < stampIDs.length; i++) {
            OrderFollowUp stamp = new OrderFollowUp();

            Integer stampId = Integer.parseInt(stampIDs[i]);

            if (stampId > 0) {
                stamp.setId(stampId);
            }

            stamp.setOrder(order);
            stamp.setOrderPhase(Order_Phase.valueOf(stampPhases[i]));
            stamp.setRemarks(stampRemark[i]);
            try {
                stamp.setTimestamp(dateFormat.parse(TimeStamp[i]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            orderFollowUps.add(stamp);
        }

    }


}
