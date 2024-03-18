package kz.laurabissoltan.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.laurabissoltan.order.dto.OrderRequest;
import kz.laurabissoltan.order.service.OrderMessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderMessagingService orderMessagingService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createOrderValidRequestReturnOk() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAmount(100.0);
        orderRequest.setEmail("test@gmail.com");

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestJson = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Order is being processed"));
    }

    @Test
    void createOrderInvalidRequestReturnsBadRequest() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAmount(-100.0);
        orderRequest.setEmail("test");

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestJson = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestJson))
                .andExpect(status().isBadRequest());
    }


}