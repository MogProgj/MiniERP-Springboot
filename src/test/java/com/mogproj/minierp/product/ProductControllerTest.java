package com.mogproj.minierp.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mogproj.minierp.common.exception.EntityNotFoundException;
import com.mogproj.minierp.common.web.GlobalExceptionHandler;
import com.mogproj.minierp.fixtures.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.SpringDataJacksonConfiguration;
import org.springframework.data.web.config.SpringDataWebSettings;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    ProductService service;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new SpringDataJacksonConfiguration.PageModule(
                new SpringDataWebSettings(EnableSpringDataWebSupport.PageSerializationMode.DIRECT)));
        var jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        ProductController controller = new ProductController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(jacksonConverter)
                .build();
    }

    @Test
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
    void GET_products_byId_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(ProductFixture.widget());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Widget"));
    }

    @Test
    void GET_products_byId_notFound_returns404() throws Exception {
        when(service.findById(99L)).thenThrow(new EntityNotFoundException("Product", 99L));

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void GET_products_list_returns200() throws Exception {
        var page = new PageImpl<>(List.of(ProductFixture.widget(), ProductFixture.gadget()));
        when(service.list(any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void DELETE_products_returns204() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }
}
