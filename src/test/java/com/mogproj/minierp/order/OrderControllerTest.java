package com.mogproj.minierp.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogproj.minierp.common.exception.BusinessRuleException;
import com.mogproj.minierp.fixtures.CustomerFixture;
import com.mogproj.minierp.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureJson
@Import(com.mogproj.minierp.security.SecurityConfig.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService service;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDraft_returnsDtoShape() throws Exception {
        var customer = CustomerFixture.alice();
        var order = new Order(customer);
        when(service.createDraft(any())).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateOrderRequest(1L))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.customer.name").value("Alice Smith"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrderById_returnsItemProductWithoutBackReference() throws Exception {
        var customer = CustomerFixture.alice();
        var order = new Order(customer);
        var product = com.mogproj.minierp.fixtures.ProductFixture.widget();
        order.addItem(new OrderItem(order, product, 2, product.getPriceCents()));
        when(service.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.email").value("alice@example.com"))
                .andExpect(jsonPath("$.items[0].qty").value(2))
                .andExpect(jsonPath("$.items[0].product.name").value("Widget"))
                .andExpect(jsonPath("$.items[0].order").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void confirm_invalidTransition_returns422() throws Exception {
        when(service.confirmOrder(1L)).thenThrow(new BusinessRuleException("Only DRAFT orders can be confirmed"));

        mockMvc.perform(post("/orders/1/confirm"))
                .andExpect(status().isUnprocessableEntity());
    }
}
