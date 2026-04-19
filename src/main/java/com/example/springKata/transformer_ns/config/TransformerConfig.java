package com.example.springKata.transformer_ns.config;

import com.example.springKata.transformer_ns.application.TransformerV1;
import com.example.springKata.transformer_ns.application.TransformerV2;
import com.example.springKata.transformer_ns.domain.port.in.TransformerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransformerConfig {

    @Value("${transformer.version:v1}")
    private String version;

    @Bean
    public TransformerService transformerService() {
        return switch (version) {
            case "v2" -> new TransformerV2();
            default   -> new TransformerV1();
        };
    }
}
