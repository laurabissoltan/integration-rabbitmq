package kz.laurabissoltan.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CoreOrderService {
    @Autowired
    private CoreOrderRepository coreOrderRepository;
    public Order createOrder(Order order) {
        return coreOrderRepository.save(order);
    }

    public List<Order> getOrders() {
        return (List<Order>) coreOrderRepository.findAll();
    }
    public Optional<Order> getOrderById(Long id) {
        return coreOrderRepository.findById(id);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = coreOrderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found"));
        order.setAmount(orderDetails.getAmount());
        order.setEmail(orderDetails.getEmail());
        return coreOrderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = coreOrderRepository.findById(id).orElseThrow(()->new RuntimeException("Order not found"));
        coreOrderRepository.deleteById(id);
    }


}
