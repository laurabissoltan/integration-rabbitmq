package kz.laurabissoltan.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.laurabissoltan.payment.dto.PaymentRequest;
import kz.laurabissoltan.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void makePaymentWhenSuccessful() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCardNumber("1111222233334444");

        Long orderId = 1L;
        String paymentRequestJson = objectMapper.writeValueAsString(paymentRequest);

        doNothing().when(paymentService).processPayment(orderId, paymentRequest);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentRequestJson)
                        .param("orderId", orderId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment processed successfully"));
    }

    @Test
    void makePaymentWhenNotSuccessful() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCardNumber("1111222233334444");

        Long orderId = 2L;
        String paymentRequestJson = objectMapper.writeValueAsString(paymentRequest);

        doThrow(new IllegalArgumentException("Order does not exist")).when(paymentService).processPayment(orderId, paymentRequest);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentRequestJson)
                        .param("orderId", orderId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Order does not exist"));
    }

}