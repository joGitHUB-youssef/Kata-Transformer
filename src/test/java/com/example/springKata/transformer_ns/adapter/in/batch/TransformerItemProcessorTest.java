package com.example.springKata.transformer_ns.adapter.in.batch;

import com.example.springKata.transformer_ns.domain.model.TransformResult;
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
    @DisplayName("Le processor doit retourner un TransformResult avec la ligne formatée pour le fichier output")
    void shouldReturnTransformResultWithFormattedLine() throws Exception {
        when(transformerService.transform(15)).thenReturn("15 \"FOOBARBAR\"");

        TransformResult result = processor.process(15);

        assertThat(result.getNumber()).isEqualTo(15);
        assertThat(result.getResult()).isEqualTo("15 \"FOOBARBAR\"");
        verify(transformerService, times(1)).transform(15);
    }

    @Test
    @DisplayName("Le processor doit déléguer la transformation au service pour chaque nombre")
    void shouldDelegateToTransformerServiceForEachNumber() throws Exception {
        when(transformerService.transform(4)).thenReturn("4 \"4\"");
        when(transformerService.transform(33)).thenReturn("33 \"FOOFOOFOO\"");

        TransformResult result1 = processor.process(4);
        TransformResult result2 = processor.process(33);

        assertThat(result1.getNumber()).isEqualTo(4);
        assertThat(result1.getResult()).isEqualTo("4 \"4\"");
        assertThat(result2.getNumber()).isEqualTo(33);
        assertThat(result2.getResult()).isEqualTo("33 \"FOOFOOFOO\"");

        verify(transformerService, times(1)).transform(4);
        verify(transformerService, times(1)).transform(33);
    }
}
