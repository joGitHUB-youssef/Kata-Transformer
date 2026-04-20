package com.example.springKata.transformer_ns.application;

import com.example.springKata.transformer_ns.domain.TransformerConstants;
import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import com.example.springKata.transformer_ns.domain.port.in.TransformerService;
import org.springframework.stereotype.Component;

/**
 * Implémentation impérative de la transformation de nombres.
 * Applique les règles métier via des conditions if/else et une boucle sur les chiffres.
 * Utilisée comme délégué interne par TransformerOptimized pour construire son cache au démarrage.
 */
@Component
public class TransformerV1 implements TransformerService {

    /**
     * @param number number to transform
     * @return String transformed number
     */
    @Override
    public String transform(int number) {
        if (number < 0 || number > 100) {
            throw new InvalidNumberException(number);
        }

        StringBuilder result = new StringBuilder();
        if (number % 3 == 0) result.append(TransformerConstants.FOO);
        if (number % 5 == 0) result.append(TransformerConstants.BAR);
        for (char digit : String.valueOf(number).toCharArray()) {
            if (digit == '3')      result.append(TransformerConstants.FOO);
            else if (digit == '5') result.append(TransformerConstants.BAR);
            else if (digit == '7') result.append(TransformerConstants.QUIX);
        }
        return result.isEmpty() ? String.valueOf(number) : result.toString();
    }
}
