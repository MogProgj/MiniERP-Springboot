package com.mogproj.minierp.customer;

import tools.jackson.databind.ObjectMapper;
import com.mogproj.minierp.common.exception.EntityNotFoundException;
import com.mogproj.minierp.fixtures.CustomerFixture;
import com.mogproj.minierp.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@AutoConfigureJson
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CustomerService service;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_customers_withValidBody_returns201() throws Exception {
        var request = new CreateCustomerRequest("Alice", "alice@example.com", null);
        var saved = CustomerFixture.alice();
        when(service.create(any())).thenReturn(saved);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_customers_withBlankName_returns400() throws Exception {
        var request = new CreateCustomerRequest("", "alice@example.com", null);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_customers_byId_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(CustomerFixture.alice());

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_customers_byId_notFound_returns404() throws Exception {
        when(service.findById(99L)).thenThrow(new EntityNotFoundException("Customer", 99L));

        mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_customers_list_returns200() throws Exception {
        var page = new PageImpl<>(List.of(CustomerFixture.alice(), CustomerFixture.bob()));
        when(service.list(any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_customers_returns204() throws Exception {
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());
    }
}
