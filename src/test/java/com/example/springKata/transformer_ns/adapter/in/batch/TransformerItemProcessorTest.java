package com.example.springKata.transformer_ns.adapter.in.batch;

import com.example.springKata.transformer_ns.domain.port.in.TransformerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransformerItemProcessorTest {

    private TransformerService transformerService;
    private TransformerItemProcessor processor;

    @BeforeEach
    void setUp() {
        transformerService = mock(TransformerService.class);
        processor = new TransformerItemProcessor(transformerService);
    }

    @Test
    @DisplayName("Le processor doit déléguer la transformation au service")
    void shouldDelegateToTransformerService() throws Exception {
        when(transformerService.transform(15)).thenReturn("FOOBARBAR");

        String result = processor.process(15);

        assertThat(result).isEqualTo("FOOBARBAR");
        verify(transformerService, times(1)).transform(15);
    }

    @Test
    @DisplayName("Le processor doit retourner le résultat du service pour chaque nombre")
    void shouldReturnServiceResultForEachNumber() throws Exception {
        when(transformerService.transform(4)).thenReturn("4");
        when(transformerService.transform(33)).thenReturn("FOOFOOFOO");

        assertThat(processor.process(4)).isEqualTo("4");
        assertThat(processor.process(33)).isEqualTo("FOOFOOFOO");

        verify(transformerService, times(1)).transform(4);
        verify(transformerService, times(1)).transform(33);
    }
}
