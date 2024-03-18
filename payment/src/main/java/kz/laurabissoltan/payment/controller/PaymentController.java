package kz.laurabissoltan.payment.controller;

import jakarta.validation.Valid;
import kz.laurabissoltan.payment.dto.PaymentRequest;
import kz.laurabissoltan.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Validated
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> makePayment(@RequestBody @Valid PaymentRequest paymentRequest,
                                              @RequestParam("orderId") Long orderID) {
        try {
            paymentService.processPayment(orderID, paymentRequest);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
