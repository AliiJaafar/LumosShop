package com.dgpad.admin.order;

import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.order.OrderFollowUp;
import com.lumosshop.common.entity.order.Order_Phase;
import com.lumosshop.common.exception.OrderNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public static final int ORDER_PER_PAGE = 9;


    public List<Order> findAll() {
        return (List<Order>) orderRepository.findAll();
    }

    public Page<Order> listOrderByPage(int pageNumber,
                                       String sortField,
                                       String sortDirection,
                                       String Keyword) {

        Sort sort = null;

        if ("summaryLocation".equals(sortField)) {
            sort = Sort.by("nation").and(Sort.by("city"));
        } else {
            sort = Sort.by(sortField);
        }


        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, ORDER_PER_PAGE, sort);


        if (Keyword != null) {
            return orderRepository.findAll(Keyword, pageable);
        }
        return orderRepository.findAll(pageable);
    }


    public void deleteById(int id) throws OrderNotFoundException {

        Long countById = orderRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new OrderNotFoundException("could not found this order id - " + id);
        }
        orderRepository.deleteById(id);

    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Order findOrderById(int id) throws OrderNotFoundException {
        try {
            return orderRepository.findById(id).get();

        } catch (NoSuchElementException e) {
            throw new OrderNotFoundException("Could not found this order id :  " + id);
        }

    }

    public void changeOrderPhase(Integer orderId, String newPhase) {

        Order orderDB = orderRepository.findById(orderId).get();
        Order_Phase orderPhase = Order_Phase.valueOf(newPhase);
        if (!orderDB.phaseExist(orderPhase)) {
            List<OrderFollowUp> orderFollowUps = orderDB.getOrderFollowUps();

            OrderFollowUp follow = new OrderFollowUp();
            follow.setOrder(orderDB);
            follow.setTimestamp(new Date());

            follow.setOrderPhase(orderPhase);
            follow.setRemarks(orderPhase.getRemark());

            orderFollowUps.add(follow);
            orderDB.setPhase(orderPhase);

            orderRepository.save(orderDB);
        }


    }


}
