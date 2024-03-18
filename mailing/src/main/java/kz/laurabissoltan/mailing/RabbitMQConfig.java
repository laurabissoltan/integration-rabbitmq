package kz.laurabissoltan.mailing;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
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
    }
   @Value("${rabbitmq.exchange}")
    private String exchangeName;
/*
    @Value("${rabbitmq.queue.orderCreated}")
    private String orderCreatedQueueName;

    @Value("${rabbitmq.routingKey.orderCreated}")
    private String orderCreatedRoutingKey;*/

/*
    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(orderCreatedQueueName, true);
    }*/


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

/*    @Bean
    public Binding binding(Queue orderCreatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(exchange).with(orderCreatedRoutingKey);
    }*/


/*    @Value("${rabbitmq.queue.orderPaid}")
    private String orderPaidQueueName;

    @Value("${rabbitmq.routingKey.orderPaid}")
    private String orderPaidRoutingKey;

    @Bean
    public Queue orderPaidQueue() {
        return new Queue(orderPaidQueueName, true);
    }

    @Bean
    public Binding bindingPaid(Queue orderPaidQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderPaidQueue).to(exchange).with(orderPaidRoutingKey);
    }*/
}
