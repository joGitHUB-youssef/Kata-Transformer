package com.example.springKata.transformer_ns.adapter.in.batch;

import com.example.springKata.transformer_ns.domain.port.in.TransformerService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("batch")
public class TransformerItemProcessor implements ItemProcessor<Integer, String> {

    private final TransformerService transformerService;

    public TransformerItemProcessor(TransformerService transformerService) {
        this.transformerService = transformerService;
    }

    @Override
    public String process(Integer number) {
        return transformerService.transform(number);
    }
}
