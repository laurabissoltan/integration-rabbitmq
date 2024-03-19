package kz.laurabissoltan.core.services;

import kz.laurabissoltan.core.dto.OrderEvent;
import kz.laurabissoltan.core.entity.Order;
import kz.laurabissoltan.core.repository.OrderRepository;
import kz.laurabissoltan.order.dto.OrderRequest;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageHandlingService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MessageSendingService messageSendingService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.queue.orderCreatedDB}", durable = "true"),
            exchange = @Exchange(value = "${rabbitmq.exchange}", type = "topic"),
            key = "${rabbitmq.routingKey.orderCreatedDB}"))
    public void receiveOrderUpdate(OrderRequest orderRequest) {

        //saving the order in the database
        Order order = new Order();
        order.setAmount(orderRequest.getAmount());
        order.setEmail(orderRequest.getEmail());
        order.setCreated(true);
        orderRepository.save(order);

        //sending message about order creation to the mailing service
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(order.getId());
        orderEvent.setEmail(order.getEmail());
        orderEvent.setStatus(orderRepository.existsById(order.getId()));
        messageSendingService.sendCreatedMessage(orderEvent);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.queue.orderExistence}", durable = "true"),
            exchange = @Exchange(value = "${rabbitmq.exchange}", type = "topic"),
            key = "${rabbitmq.routingKey.orderExistence}"))
    public Boolean receiveOrderCheck(Long orderId) { // Adjust parameter type as needed based on your message conversion setup
        return orderRepository.existsById(orderId);
    }

  //  @RabbitListener(queues = "${rabbitmq.queue.paymentStatusUpdate}")

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.queue.paymentStatusUpdate}", durable = "true"),
            exchange = @Exchange(value = "${rabbitmq.exchange}", type = "topic"),
            key = "${rabbitmq.routingKey.paymentStatusUpdate}"))
    public void handlePaymentStatus(String message) {
        String[] parts = message.split(",");
        Long orderId = Long.valueOf(parts[0]);
        boolean paymentStatus = Boolean.parseBoolean(parts[1]);

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if (!order.isPaid() && paymentStatus) {
                //updating database
                order.setPaid(paymentStatus);
                orderRepository.save(order);

                //sending message to the mailing
                OrderEvent orderEvent = new OrderEvent();
                orderEvent.setOrderId(order.getId());
                orderEvent.setEmail(order.getEmail());
                orderEvent.setStatus(paymentStatus);
                messageSendingService.sendPaidMessage(orderEvent);
            } else {
                System.out.println("Order with ID " + orderId + " is already paid or the paymentStatus is false.");
            }
        } else {
            System.out.println("Order with ID " + orderId + " not found.");
        }
    }
/*    public void handlePaymentStatus(String message) {
        String[] parts = message.split(",");
        Long orderId = Long.valueOf(parts[0]);
        boolean paymentStatus = Boolean.parseBoolean(parts[1]);

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setPaid(paymentStatus);
            orderRepository.save(order);

            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrderId(order.getId());
            orderEvent.setEmail(order.getEmail());
            orderEvent.setStatus(paymentStatus);
            messageSendingService.sendPaidMessage(orderEvent);
        } else {
            System.out.println("Order with ID " + orderId + " not found.");
        }
    }*/
}
