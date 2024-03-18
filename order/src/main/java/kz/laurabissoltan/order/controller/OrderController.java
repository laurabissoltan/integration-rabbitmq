package kz.laurabissoltan.order.controller;

import jakarta.validation.Valid;
import kz.laurabissoltan.order.dto.OrderRequest;
import kz.laurabissoltan.order.service.OrderMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderMessagingService orderMessagingService;

    @PostMapping()
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        orderMessagingService.sendOrder(orderRequest);
        return ResponseEntity.ok("Order is being processed");
    }
}
