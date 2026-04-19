package com.example.springKata.transformer_ns.adapter.in.batch;

import com.example.springKata.transformer_ns.domain.model.TransformResult;
import com.example.springKata.transformer_ns.domain.port.in.TransformerService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("batch")
public class TransformerItemProcessor implements ItemProcessor<Integer, TransformResult> {

    private final TransformerService transformerService;

    public TransformerItemProcessor(@Qualifier("batchTransformer") TransformerService transformerService) {
        this.transformerService = transformerService;
    }

    @Override
    public TransformResult process(Integer number) {
        return new TransformResult(number, transformerService.transform(number));
    }
}
