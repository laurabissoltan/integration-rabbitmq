package kz.laurabissoltan.order.service;

import kz.laurabissoltan.order.dto.OrderRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderMessagingServiceTest {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingKey.orderCreatedDB}")
    private String routingKey;

    @Autowired
    private OrderMessagingService orderMessagingService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAmount(100.0);
        orderRequest.setEmail("example@gmail.com");

        orderMessagingService.sendOrder(orderRequest);

        Mockito.verify(rabbitTemplate, Mockito.times(1))
                .convertAndSend(
                        Mockito.eq(exchange),
                        Mockito.eq(routingKey),
                        Mockito.eq(orderRequest));
    }
}