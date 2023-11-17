package com.dgpad.order;

import com.dgpad.checkout.CheckoutModel;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import com.lumosshop.common.entity.ShoppingBag;
import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.order.Order_Phase;
import com.lumosshop.common.entity.order.Order_Summary;
import com.lumosshop.common.entity.order.Payment_Choice;
import com.lumosshop.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;


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
            Product product = stock.getProduct();

            Order_Summary orderSummary = new Order_Summary();
            orderSummary.setOrder(createdOrder);
            orderSummary.setProduct(product);
            orderSummary.setQty(stock.getQuantity());
            orderSummary.setItemPrice(product.getDiscountPrice());
            orderSummary.setProductCost(product.getCost() * stock.getQuantity());
            orderSummary.setInterSum(stock.getThePriceMultiplyByQty());
            orderSummary.setShippingCharge(stock.getShippingCharge());

            orderSummaries.add(orderSummary);
        }

        return orderRepository.save(createdOrder);
    }
}
