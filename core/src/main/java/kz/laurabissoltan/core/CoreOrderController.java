package kz.laurabissoltan.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
public class CoreOrderController {
    @Autowired
    CoreOrderService coreOrderService;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return coreOrderService.createOrder(order);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = coreOrderService.getOrderById(id).orElseThrow(()-> new RuntimeException("Order not found"));
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return coreOrderService.getOrders();
    }

    @PostMapping("/orders/{id}")
    public ResponseEntity<Order> updataOrder(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = coreOrderService.updateOrder(id, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable Long id) {
        coreOrderService.deleteOrder(id);
    }
}
