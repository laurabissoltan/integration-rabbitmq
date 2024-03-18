package kz.laurabissoltan.payment.service;

import kz.laurabissoltan.payment.dto.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingKey.orderExistence}")
    private String orderExistenceRoutingKey;

    @Value("${rabbitmq.routingKey.paymentStatusUpdate}")
    private String statusUpdateRoutingKey;

    @Test
    void checkOrderExistenceWhenOrderExists() {
        Long orderId = 1L;
        when(rabbitTemplate.convertSendAndReceive(exchange, orderExistenceRoutingKey, orderId)).thenReturn(true);

        boolean exists = paymentService.checkOrderExistence(orderId);

        assertTrue(exists);
        verify(rabbitTemplate, times(1)).convertSendAndReceive(exchange, orderExistenceRoutingKey, orderId);
    }

    @Test
    void checkOrderExistenceWhenOrderDoNotExist() {
        Long orderId = 1L;
        when(rabbitTemplate.convertSendAndReceive(exchange, orderExistenceRoutingKey, orderId)).thenReturn(false);

        boolean exists = paymentService.checkOrderExistence(orderId);

        assertFalse(exists);
        verify(rabbitTemplate, times(1)).convertSendAndReceive(exchange, orderExistenceRoutingKey, orderId);
    }

    @Test
    void sendPaymentStatusToTheCoreWhenPaid() {
        Long orderId = 1L;
        boolean paymentStatus = true;
        String message = orderId + "," + paymentStatus;

        paymentService.sendPaymentStatus(orderId, paymentStatus);

        verify(rabbitTemplate, times(1)).convertAndSend(exchange, statusUpdateRoutingKey, message);
    }

    @Test
    void processPaymentWhenOrderDoesNotExist() {
        Long orderId = 1L;
        PaymentRequest paymentRequest = new PaymentRequest();
     //   String message = orderId + ",false";
        paymentRequest.setCardNumber("1111222233334444");
        when(rabbitTemplate.convertSendAndReceive(exchange, statusUpdateRoutingKey, orderId)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
        ()-> {paymentService.processPayment(orderId, paymentRequest);
        });

        assertEquals("Order does not exist", exception.getMessage());
        verify(rabbitTemplate, times(1)).convertSendAndReceive(exchange, orderExistenceRoutingKey, orderId);
        verify(rabbitTemplate, times(1)).convertAndSend(exchange, statusUpdateRoutingKey, orderId + ",false");
    }

    @Test
    void processPaymentWhenOrderExists() {
        Long orderId = 1L;
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCardNumber("1234567890123456");
        when(rabbitTemplate.convertSendAndReceive(exchange, orderExistenceRoutingKey, orderId)).thenReturn(true);


        paymentService.processPayment(orderId, paymentRequest);


        verify(rabbitTemplate, times(1)).convertSendAndReceive(exchange, orderExistenceRoutingKey, orderId);
        verify(rabbitTemplate, times(1)).convertAndSend(exchange, statusUpdateRoutingKey, orderId + ",true");
    }

}