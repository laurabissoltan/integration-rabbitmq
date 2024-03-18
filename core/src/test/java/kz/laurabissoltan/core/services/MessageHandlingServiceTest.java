package kz.laurabissoltan.core.services;

import kz.laurabissoltan.core.dto.OrderEvent;
import kz.laurabissoltan.core.entity.Order;
import kz.laurabissoltan.core.repository.OrderRepository;
import kz.laurabissoltan.order.dto.OrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageHandlingServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MessageSendingService messageSendingService;

    @InjectMocks
    private MessageHandlingService messageHandlingService;


    @Test
    void testReceiveOrderCheck() {
        Long orderId = 1L;
        when(orderRepository.existsById(orderId)).thenReturn(true);

        boolean exists = messageHandlingService.receiveOrderCheck(orderId);

        assertTrue(exists);
        verify(orderRepository, times(1)).existsById(orderId);
    }

    @Test
    void testHandlePaymentStatusOrderExists() {
        Long orderId = 1L;
        String message = orderId + ",true";

        Order order = new Order();
        order.setId(orderId);
        order.setEmail("customer@example.com");
        order.setPaid(false);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        messageHandlingService.handlePaymentStatus(message);

        verify(orderRepository, times(1)).save(order);
        assertTrue(order.isPaid());
        verify(messageSendingService, times(1)).sendPaidMessage(any(OrderEvent.class));
    }
}