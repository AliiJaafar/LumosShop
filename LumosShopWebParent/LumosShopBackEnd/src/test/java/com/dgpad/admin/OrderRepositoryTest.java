package com.dgpad.admin;

import com.dgpad.admin.Shipping.ShippingRepository;
import com.dgpad.admin.Shipping.ShippingService;
import com.dgpad.admin.order.OrderRepository;
import com.dgpad.admin.product.ProductRepository;
import com.lumosshop.common.entity.Category;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Shipping;
import com.lumosshop.common.entity.order.*;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.ShippingFeeNotFoundException;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateOrder() {

        Customer customer = entityManager.find(Customer.class, 1);
        Product product = entityManager.find(Product.class, 1);

        Order mainOrder = new Order();
        mainOrder.setCustomer(customer);
        mainOrder.setFirstName(customer.getFirstName());
        mainOrder.setLastName(customer.getLastName());
        mainOrder.setPhoneNumber(customer.getPhoneNumber());
        mainOrder.setAddressLine1(customer.getAddressLine1());
        mainOrder.setAddressLine2(customer.getAddressLine2());
        mainOrder.setAddressLine2(customer.getAddressLine2());
        mainOrder.setCity(customer.getCity());
        mainOrder.setNation(customer.getNation().getName());

        mainOrder.setShippingCharge(20);
        mainOrder.setProductCost(product.getCost());
        mainOrder.setVAT(0);
        mainOrder.setInterSum(product.getPrice());
        mainOrder.setTotalPrice(product.getPrice() + 10);

        mainOrder.setPaymentChoice(Payment_Choice.PAYPAL);
        mainOrder.setPhase(Order_Phase.NEW);
        mainOrder.setOrderDate(new Date());
        mainOrder.setDeliverDate(new Date());
        mainOrder.setNumberOfDaysToDeliver(2);


        Order_Summary orderSummary = new Order_Summary();
        orderSummary.setOrder(mainOrder);
        orderSummary.setProduct(product);
        orderSummary.setProductCost(product.getCost());
        orderSummary.setShippingCharge(10);
        orderSummary.setQty(1);
        orderSummary.setItemPrice(product.getPrice());
        orderSummary.setInterSum(product.getPrice());

        mainOrder.getOrderSummaries().add(orderSummary);

        Order newSavedOrder = orderRepository.save(mainOrder);
        assertThat(newSavedOrder.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateNewOrderWithMultipleProducts() {
        Customer customer = entityManager.find(Customer.class, 35);
        Product product1 = entityManager.find(Product.class, 2);
        Product product2 = entityManager.find(Product.class, 4);

        Order mainOrder = new Order();
        mainOrder.setOrderDate(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.CustomerAddress();

        Order_Summary orderSummary = new Order_Summary();
        orderSummary.setProduct(product1);
        orderSummary.setOrder(mainOrder);
        orderSummary.setProductCost(product1.getCost());
        orderSummary.setShippingCharge(10);
        orderSummary.setQty(1);
        orderSummary.setInterSum(product1.getPrice());
        orderSummary.setItemPrice(product1.getPrice());

        Order_Summary orderSummary2 = new Order_Summary();
        orderSummary2.setProduct(product2);
        orderSummary2.setOrder(mainOrder);
        orderSummary2.setProductCost(product2.getCost());
        orderSummary2.setShippingCharge(20);
        orderSummary2.setQty(2);
        orderSummary2.setInterSum(product2.getPrice() * 2);
        orderSummary2.setItemPrice(product2.getPrice());

        mainOrder.getOrderSummaries().add(orderSummary);
        mainOrder.getOrderSummaries().add(orderSummary2);

        mainOrder.setShippingCharge(30);
        mainOrder.setProductCost(product1.getCost() + product2.getCost());
        mainOrder.setVAT(0);
        float subtotal = product1.getPrice() + product2.getPrice() * 2;
        mainOrder.setInterSum(subtotal);
        mainOrder.setTotalPrice(subtotal + 30);

        mainOrder.setPaymentChoice(Payment_Choice.CASH_ON_DELIVERY);
        mainOrder.setPhase(Order_Phase.PACKAGED);
        mainOrder.setDeliverDate(new Date());
        mainOrder.setNumberOfDaysToDeliver(3);

        Order savedOrder = orderRepository.save(mainOrder);
        assertThat(savedOrder.getId()).isGreaterThan(0);
    }

    @Test
    public void testListOrders() {
        Iterable<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSizeGreaterThan(0);

        orders.forEach(System.out::println);
    }

    @Test
    public void OrderFollowUp() {
        Integer OrderId = 1;
        Order order = orderRepository.findById(1).get();
        OrderFollowUp record = new OrderFollowUp();
        record.setOrder(order);
        record.setOrderPhase(Order_Phase.IN_TRANSIT);
        record.setRemarks(Order_Phase.IN_TRANSIT.getRemark());
        record.setTimestamp(new Date());
        List<OrderFollowUp> orderFollowUps = order.getOrderFollowUps();
        orderFollowUps.add(record);

        orderRepository.save(order);
    }
    @Test
    public void DetermineShippingCharge() throws ShippingFeeNotFoundException {

        Integer productID = 1;
        Integer NationID = 19;
        String city = "Beirut";
        Shipping shipping = shippingRepository.findByNationAndCity(NationID, city);
        System.out.println(shipping);

    }
}