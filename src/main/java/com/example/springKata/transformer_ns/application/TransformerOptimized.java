package com.example.springKata.transformer_ns.application;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import com.example.springKata.transformer_ns.domain.port.in.TransformerService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implémentation optimisée avec pré-calcul de toutes les valeurs possibles (0 à 100) au démarrage.
 * Délègue le calcul initial à TransformerV1, puis stocke les résultats en cache pour une réponse en O(1).
 * C'est l'implémentation par défaut injectée dans le controller REST (@Primary).
 */
@Component
@Primary
public class TransformerOptimized implements TransformerService {

    private final Map<Integer, String> cache;

    public TransformerOptimized(TransformerV1 delegate) {
        this.cache = IntStream.rangeClosed(0, 100)
                .boxed()
                .collect(Collectors.toMap(n -> n, delegate::transform));
    }

    /**
     * @param number number to transform
     * @return String transformed number
     */
    @Override
    public String transform(int number) {
        if (number < 0 || number > 100) {
            throw new InvalidNumberException(number);
        }
        return cache.get(number);
    }
}
