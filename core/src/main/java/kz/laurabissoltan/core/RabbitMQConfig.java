package kz.laurabissoltan.core;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
/*    @Value("${rabbitmq.exchange}")
    private String exchange;*/

    // Configure RabbitTemplate with a custom MessageConverter to use JSON
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    // JSON message converter for serializing and deserializing messages
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Define the exchange that the application will use for sending messages
/*    @Bean
    public TopicExchange Exchange() {
        return new TopicExchange(exchange);
    }

    @Value("${rabbitmq.queue.orderCreatedQueueToCore}")
    private String orderCreatedQueue;

    @Value("${rabbitmq.routingKey.orderCreatedToCore}")
    private String orderCreatedRoutingKey;

    @Value("${rabbitmq.exchange.order}")
    private String orderCoreExchange;

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(orderCreatedQueue, true);
    }

    @Bean
    public Binding bindingCreated(Queue orderPaidQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderPaidQueue).to(exchange).with(orderCreatedRoutingKey);
    }*/

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}

