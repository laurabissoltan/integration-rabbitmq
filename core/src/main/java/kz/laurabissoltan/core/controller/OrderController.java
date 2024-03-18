package kz.laurabissoltan.core.controller;

import kz.laurabissoltan.core.entity.Order;
import kz.laurabissoltan.core.services.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
public class OrderController {
    @Autowired
    OrderManagementService orderManagementService;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderManagementService.createOrder(order);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderManagementService.getOrderById(id).orElseThrow(()-> new RuntimeException("Order not found"));
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderManagementService.getOrders();
    }

    @PostMapping("/orders/{id}")
    public ResponseEntity<Order> updataOrder(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = orderManagementService.updateOrder(id, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderManagementService.deleteOrder(id);
    }
}
