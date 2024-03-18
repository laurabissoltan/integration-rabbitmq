package kz.laurabissoltan.order.service;

import kz.laurabissoltan.order.dto.OrderRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderMessagingService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String orderCoreExchange;

    @Value("${rabbitmq.routingKey.orderCreatedDB}")
    private String orderCoreRoutingKey;

    public OrderMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate=rabbitTemplate;
    }
    public void sendOrder(OrderRequest orderRequest) {
        rabbitTemplate.convertAndSend(orderCoreExchange, orderCoreRoutingKey, orderRequest);
    }
}
