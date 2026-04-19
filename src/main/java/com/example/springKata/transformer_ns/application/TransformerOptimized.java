package com.example.springKata.transformer_ns.application;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import com.example.springKata.transformer_ns.domain.port.in.TransformerService;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransformerOptimized implements TransformerService {

    private final Map<Integer, String> cache;

    public TransformerOptimized() {
        TransformerService delegate = new TransformerV1();
        this.cache = IntStream.rangeClosed(0, 100)
                .boxed()
                .collect(Collectors.toMap(n -> n, delegate::transform));
    }

    @Override
    public String transform(int number) {
        if (number < 0 || number > 100) {
            throw new InvalidNumberException(number);
        }
        return cache.get(number);
    }
}
