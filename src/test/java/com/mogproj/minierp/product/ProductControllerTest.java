package com.mogproj.minierp.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogproj.minierp.common.exception.EntityNotFoundException;
import com.mogproj.minierp.fixtures.ProductFixture;
import com.mogproj.minierp.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

@WebMvcTest(ProductController.class)
@AutoConfigureJson
@Import(com.mogproj.minierp.security.SecurityConfig.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService service;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_products_withValidBody_returns201() throws Exception {
        var request = new CreateProductRequest("SKU-001", "Widget", 999);
        var saved = ProductFixture.widget();
        when(service.create(any())).thenReturn(saved);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_products_withNegativePrice_returns400() throws Exception {
        var request = new CreateProductRequest("SKU-001", "Widget", -1);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_products_byId_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(ProductFixture.widget());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Widget"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_products_byId_notFound_returns404() throws Exception {
        when(service.findById(99L)).thenThrow(new EntityNotFoundException("Product", 99L));

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_products_list_returns200() throws Exception {
        var page = new PageImpl<>(List.of(ProductFixture.widget(), ProductFixture.gadget()));
        when(service.list(any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_products_returns204() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }
}
