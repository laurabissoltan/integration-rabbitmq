package kz.laurabissoltan.payment;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingKey.orderExistence}")
    private String orderExistenceRoutingKey;

    @Value("${rabbitmq.routingKey.paymentStatusUpdate}")
    private String statusUpdateRoutingKey;

    public boolean checkOrderExistence(Long orderId) {
        Boolean exists = (Boolean) rabbitTemplate.convertSendAndReceive(
                exchange, orderExistenceRoutingKey, orderId);
        return exists != null && exists;
    }
    public void sendPaymentStatus(Long orderId, boolean paymentStatus) {
        String message = orderId + "," + paymentStatus;
        rabbitTemplate.convertAndSend(exchange, statusUpdateRoutingKey, message);
    }

    public void processPayment(Long orderId, PaymentRequest paymentRequest) {
        boolean orderExists = checkOrderExistence(orderId);
        if (!orderExists) {
            sendPaymentStatus(orderId, false);
            throw new IllegalArgumentException("Order does not exist.");
        }

        System.out.println("Processing payment with card number: " + paymentRequest.getCardNumber());
        sendPaymentStatus(orderId, true);
    }

}

