package kz.laurabissoltan.payment;

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
    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.queue.paymentStatusUpdate}")
    private String paymentStatusUpdateQueue;

    @Value("${rabbitmq.routingKey.paymentStatusUpdate}")
    private String paymentStatusUpdateRoutingKey;

    @Value("${rabbitmq.queue.orderExistence}")
    private String orderExistenceQueue;

    @Value("${rabbitmq.routingKey.orderExistence}")
    private String orderExistenceRoutingKey;

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue paymentStatusUpdateQueue() {
        return new Queue(paymentStatusUpdateQueue, true);
    }

    @Bean
    public Binding paymentStatusUpdateBinding(Queue paymentStatusUpdateQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentStatusUpdateQueue).to(paymentExchange).with(paymentStatusUpdateRoutingKey);
    }

    @Bean
    public Queue orderExistenceQueue() {
        return new Queue(orderExistenceQueue, true);
    }

    @Bean
    public Binding orderExistenceBinding(Queue orderExistenceQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(orderExistenceQueue).to(paymentExchange).with(orderExistenceRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, final MessageConverter jsonMessageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
/*

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    // Queues this service might listen to (if applicable)
    @Value("${core.and.payment.queue}")
    private String paymentToCoreQueueName;

    // For sending payment status updates
    @Value("${payment.status.update.queue}")
    private String paymentStatusUpdateQueueName;

    @Value("${payment.status.update.routing.key}")
    private String paymentStatusUpdateRoutingKey;

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(exchangeName);
    }

    // Queue for listening (if the payment service needs to listen to messages)
    @Bean
    public Queue paymentToCoreQueue() {
        return new Queue(paymentToCoreQueueName, true);
    }

    @Bean
    public Queue paymentStatusUpdateQueue() {
        return new Queue(paymentStatusUpdateQueueName, true);
    }

    // Binding for listening queue (if needed)
    @Bean
    public Binding paymentToCoreBinding(Queue paymentToCoreQueue, TopicExchange paymentExchange) {
        // Assuming a routing key for payment to core messages
        return BindingBuilder.bind(paymentToCoreQueue).to(paymentExchange).with("payment.to.core");
    }

    // This service primarily sends messages, so it might not need a listener setup unless it's receiving acknowledgments
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
                                         final MessageConverter jsonMessageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Listener container factory setup if this service needs to listen to messages
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

    // If there are outbound messages that require a specific setup, define them here
    // Example: Sending payment status updates
    @Bean
    public Binding paymentStatusUpdateBinding(Queue paymentStatusUpdateQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentStatusUpdateQueue).to(paymentExchange).with(paymentStatusUpdateRoutingKey);
    }
*/

   /* @Value("${core.and.payment.queue}")
    private String jsonQueue;

    @Value("${core.and.payment.exchange}")
    private String exchange;

    @Value("${core.and.payment.routing.key}")
    private String jsonRoutingKey;

    @Bean
    public Queue jsonQueue() {
        return new Queue(jsonQueue);
    }


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding jsonBinding() {
        return BindingBuilder.bind(jsonQueue())
                .to(exchange())
                .with(jsonRoutingKey);
    }


    //RabbitMQ Listener
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Value("${payment.status.update.exchange}")
    private String statusUpdateExchange;

    @Value("${payment.status.update.queue}")
    private String statusUpdateQueue;

    @Value("${payment.status.update.routing.key}")
    private String statusUpdateRoutingKey;

    @Bean
    public Queue statusUpdateQueue() {
        return new Queue(statusUpdateQueue);
    }

    @Bean
    public DirectExchange statusUpdateExchange() {
        return new DirectExchange(statusUpdateExchange);
    }

    @Bean
    public Binding statusUpdateBinding() {
        return BindingBuilder.bind(statusUpdateQueue())
                .to(statusUpdateExchange())
                .with(statusUpdateRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }*/
}
