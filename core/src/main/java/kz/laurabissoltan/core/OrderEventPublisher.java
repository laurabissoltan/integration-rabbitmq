package kz.laurabissoltan.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingKey.orderCreatedNotify}")
    private String orderCreatedRoutingKey;

    @Value("${rabbitmq.routingKey.orderPaidNotify}")
    private String orderPaidRoutingKey;


    public void sendCreatedMessage(OrderEvent orderEvent) {
        try {
            String message = objectMapper.writeValueAsString(orderEvent);
            rabbitTemplate.convertAndSend(exchange, orderCreatedRoutingKey, message);
            System.out.println("Order creation message sent");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send order creation message");
        }
    }

    public void sendPaidMessage(OrderEvent orderEvent) {
        try {
            String message = objectMapper.writeValueAsString(orderEvent);
            rabbitTemplate.convertAndSend(exchange, orderPaidRoutingKey, message);
            System.out.println("Order creation message sent");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send order creation message");
        }
    }
}
