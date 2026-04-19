package com.example.springKata.transformer_ns.application;

import com.example.springKata.transformer_ns.domain.TransformerConstants;
import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import com.example.springKata.transformer_ns.domain.port.in.TransformerService;

import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class TransformerV2 implements TransformerService {

    private record DigitRule(char digit, String token) {}

    private static final List<DigitRule> DIGIT_RULES = List.of(
        new DigitRule('3', TransformerConstants.FOO),
        new DigitRule('5', TransformerConstants.BAR),
        new DigitRule('7', TransformerConstants.QUIX)
    );

    @Override
    public String transform(int number) {
        if (number < 0 || number > 100) {
            throw new InvalidNumberException(number);
        }

        String divisible = (number % 3 == 0 ? TransformerConstants.FOO : "")
                         + (number % 5 == 0 ? TransformerConstants.BAR : "");

        String fromDigits = String.valueOf(number).chars()
            .mapToObj(c -> DIGIT_RULES.stream()
                .filter(r -> r.digit() == (char) c)
                .map(DigitRule::token)
                .findFirst()
                .orElse(""))
            .reduce("", String::concat);

        String result = divisible + fromDigits;
        return result.isEmpty() ? String.valueOf(number) : result;
    }
}
