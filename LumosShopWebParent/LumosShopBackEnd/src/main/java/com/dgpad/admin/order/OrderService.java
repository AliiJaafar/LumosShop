package com.dgpad.admin.order;

import com.lumosshop.common.entity.order.Order;
import com.lumosshop.common.entity.order.Order_Phase;
import com.lumosshop.common.exception.OrderNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public void updateOrderPhase(Integer orderId, Order_Phase newPhase) throws OrderNotFoundException {

        try {
            Order order = orderRepository.findById(orderId).get();
            orderRepository.updatePhase(orderId, newPhase);
        } catch (NoSuchElementException e) {
            throw new OrderNotFoundException("Could not found this order id :  " + orderId);

        }


    }





}
