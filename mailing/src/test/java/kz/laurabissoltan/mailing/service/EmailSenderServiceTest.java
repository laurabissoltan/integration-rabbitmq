package kz.laurabissoltan.mailing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.laurabissoltan.mailing.dto.OrderMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @Test
    void receiveOrderCreatedMessageSuccessful() throws IOException {
        String jsonMessage = "{\"email\":\"example@gmail.com\",\"orderId\":\"1L\"}";
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setEmail("example@gmail.com");
        orderMessage.setOrderId(1L);
        orderMessage.setStatus(true);

        when(objectMapper.readValue(jsonMessage, OrderMessage.class)).thenReturn(orderMessage);

        emailSenderService.receiveOrderCreatedMessage(jsonMessage);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void receiveOrderPaidMessageSuccessful() throws IOException {
        String jsonMessage = "{\"email\":\"example@gmail.com\",\"orderId\":\"1L\"}";
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setEmail("example@gmail.com");
        orderMessage.setOrderId(1L);
        orderMessage.setStatus(true);

        when(objectMapper.readValue(jsonMessage, OrderMessage.class)).thenReturn(orderMessage);

        emailSenderService.receiveOrderPaidMessage(jsonMessage);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

}