package kz.laurabissoltan.core.services;

import kz.laurabissoltan.core.entity.Order;
import kz.laurabissoltan.core.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class OrderManagementService {
    @Autowired
    private OrderRepository orderRepository;
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getOrders() {
        return (List<Order>) orderRepository.findAll();
    }
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found"));
        order.setAmount(orderDetails.getAmount());
        order.setEmail(orderDetails.getEmail());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()->new RuntimeException("Order not found"));
        orderRepository.deleteById(id);
    }


}
