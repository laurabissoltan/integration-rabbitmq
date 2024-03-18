package kz.laurabissoltan.mailing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    public boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }
    public void sendEmail(String to, String subject, String text) {
        if (!isValidEmailAddress(to)) {
            System.err.println("Attempted to send an email to an invalid address: " + to);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmailAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    @Autowired
    private ObjectMapper objectMapper;

//  @RabbitListener(queues = "${rabbitmq.queue.orderCreated}")

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.queue.orderCreatedNotify}", durable = "true"),
            exchange = @Exchange(value = "${rabbitmq.exchange}", type = "topic"),
            key = "${rabbitmq.routingKey.orderCreatedNotify}"))
    public void receiveOrderCreatedMessage(String message) {
        try {
          //  System.out.println("received message in the mailing" + message);
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);

            sendEmail(orderMessage.getEmail(), "Order Creation Status",
                    "Dear Customer, your order with ID " + orderMessage.getOrderId() + " has been processed.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to deserialize order creation message");
        }
    }

 //   @RabbitListener(queues = "${rabbitmq.queue.orderPaid}")
       @RabbitListener(bindings = @QueueBinding(
         value = @Queue(value = "${rabbitmq.queue.orderPaidNotify}", durable = "true"),
         exchange = @Exchange(value = "${rabbitmq.exchange}", type = "topic"),
         key = "${rabbitmq.routingKey.orderPaidNotify}"))
    public void receiveOrderPaidMessage(String message) {
        try {
          //  System.out.println("received message in the mailing" + message);
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);

            sendEmail(orderMessage.getEmail(), "Order Payment Status",
                    "Dear customer, your order with ID " + orderMessage.getOrderId() + " has been paid.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to deserialize order creation message");
        }
    }
}

