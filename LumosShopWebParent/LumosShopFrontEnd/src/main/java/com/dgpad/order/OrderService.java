package com.dgpad.order;

import com.dgpad.checkout.CheckoutModel;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import com.lumosshop.common.entity.ShoppingBag;
import com.lumosshop.common.entity.order.*;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;


    public static final int NUMBER_OF_ORDER_IN_PAGE = 4;

    public Order newOrder(Payment_Choice paymentChoice, Customer customer, List<ShoppingBag> bagStocks, CustomerAddresses address,
                             CheckoutModel checkoutModel) {

        Order createdOrder = new Order();
        createdOrder.setOrderDate(new Date());

        if (paymentChoice.equals(Payment_Choice.CASH_ON_DELIVERY)) {
            createdOrder.setPhase(Order_Phase.NEW);
        } else {
            createdOrder.setPhase(Order_Phase.PAID);
        }

        createdOrder.setVAT(0.0f);
        createdOrder.setTotalPrice(checkoutModel.getBillTotal());
        createdOrder.setPaymentChoice(paymentChoice);
        createdOrder.setNumberOfDaysToDeliver(checkoutModel.getDeliveryETA());
        createdOrder.setDeliverDate(checkoutModel.getShippingDate());
        createdOrder.setCustomer(customer);
        createdOrder.setProductCost(checkoutModel.getCost());
        createdOrder.setInterSum(checkoutModel.getTotalPrice());
        createdOrder.setShippingCharge(checkoutModel.getShippingCharge());


        if (address == null) {
            createdOrder.CustomerAddress();
        } else {
            createdOrder.ShippingAddress(address);
        }

        Set<Order_Summary> orderSummaries = createdOrder.getOrderSummaries();

        for (ShoppingBag stock : bagStocks) {
            Order_Summary orderSummary = getOrderSummary(stock, createdOrder);

            orderSummaries.add(orderSummary);
        }
        OrderFollowUp followUp = new OrderFollowUp();
        followUp.setOrder(createdOrder);
        followUp.setOrderPhase(Order_Phase.NEW);
        followUp.setRemarks(Order_Phase.NEW.getRemark());
        followUp.setTimestamp(new Date());

        createdOrder.getOrderFollowUps().add(followUp);

        return orderRepository.save(createdOrder);
    }

    public Order retrieveOrder(Customer customer, Integer ID) {
        return orderRepository.findByIdAndCustomer(ID, customer);
    }
    public Order findOrderById(int id) throws OrderNotFoundException {
        try {
            return orderRepository.findById(id).get();

        } catch (NoSuchElementException e) {
            throw new OrderNotFoundException("Could not found this order id :  " + id);
        }

    }

    private static Order_Summary getOrderSummary(ShoppingBag stock, Order createdOrder) {
        Product product = stock.getProduct();

        Order_Summary orderSummary = new Order_Summary();
        orderSummary.setOrder(createdOrder);
        orderSummary.setProduct(product);
        orderSummary.setQty(stock.getQuantity());
        orderSummary.setItemPrice(product.getDiscountPrice());
        orderSummary.setProductCost(product.getCost() * stock.getQuantity());
        orderSummary.setInterSum(stock.getThePriceMultiplyByQty());
        orderSummary.setShippingCharge(stock.getShippingCharge());
        return orderSummary;
    }

    public Page<Order> displayAllOrderByPage(int pageNumber,
                                             String sortField,
                                             String sortDirection,
                                             String keyword,
                                             Customer customer){
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, NUMBER_OF_ORDER_IN_PAGE, sort);
        if (keyword != null) {
            return orderRepository.findAllByCustomerAndKeyword(keyword, customer.getId(), pageable);
        }
        return orderRepository.findAllByCustomer(customer.getId(), pageable);
    }


    public void setOrderCUSTOMER_REQUESTED_RETURN(Customer customer,
                                                  Request_Reverted requestReverted) throws OrderNotFoundException {

        Order order = orderRepository.findByIdAndCustomer(requestReverted.getOrderID(), customer);
        if (order == null) {
            throw new OrderNotFoundException("Order ID #" + requestReverted.getOrderID() + " Couldn't be found.");
        }

        if (order.isCUSTOMER_REQUESTED_RETURN()) {
            return;
        }
        OrderFollowUp followUp = new OrderFollowUp();
        followUp.setOrder(order);
        followUp.setOrderPhase(Order_Phase.CUSTOMER_REQUESTED_RETURN);
        followUp.setTimestamp(new Date());
        String remark = "Justification is " + requestReverted.getJustification() + "; ";
        remark += handleNull(requestReverted.getComment());
        followUp.setRemarks(remark);

        order.getOrderFollowUps().add(followUp);
        order.setPhase(Order_Phase.CUSTOMER_REQUESTED_RETURN);

        orderRepository.save(order);
    }
    private String handleNull(String value) {
        return (value != null) ? value : ".";
    }

}

