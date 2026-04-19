package com.example.springKata.transformer_ns.adapter.in.web;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import com.example.springKata.transformer_ns.domain.port.in.TransformerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransformerController.class)
class TransformerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransformerService transformerService;

    @Test
    @DisplayName("GET /transform/15 doit retourner le résultat transformé")
    void shouldReturnTransformedResultForValidNumber() throws Exception {
        when(transformerService.transform(15)).thenReturn("FOOBARBAR");

        mockMvc.perform(get("/transform/15")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("FOOBARBAR"));
    }

    @Test
    @DisplayName("GET /transform/33 doit retourner FOOFOOFOO")
    void shouldReturnFooFooFooFor33() throws Exception {
        when(transformerService.transform(33)).thenReturn("FOOFOOFOO");

        mockMvc.perform(get("/transform/33")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("FOOFOOFOO"));
    }

    @Test
    @DisplayName("GET /transform/-1 doit retourner 400")
    void shouldReturn400ForNegativeNumber() throws Exception {
        when(transformerService.transform(-1)).thenThrow(new InvalidNumberException(-1));

        mockMvc.perform(get("/transform/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Number must be between 0 and 100, got: -1"));
    }

    @Test
    @DisplayName("GET /transform/101 doit retourner 400")
    void shouldReturn400ForNumberAbove100() throws Exception {
        when(transformerService.transform(101)).thenThrow(new InvalidNumberException(101));

        mockMvc.perform(get("/transform/101")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Number must be between 0 and 100, got: 101"));
    }
}
