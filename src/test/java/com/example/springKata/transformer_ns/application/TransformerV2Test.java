package com.example.springKata.transformer_ns.application;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransformerV2Test {

    private final TransformerV2 transformer = new TransformerV2();

    @Test
    @DisplayName("Un nombre sans règle doit retourner la ligne formatée pour le fichier output")
    void shouldReturnFormattedLineWhenNoRuleMatches() {
        assertThat(transformer.transform(4)).isEqualTo("4 \"4\"");
    }

    @Test
    @DisplayName("Un nombre divisible par 3 doit retourner la ligne formatée avec FOO")
    void shouldReturnFormattedLineWithFooWhenDivisibleByThree() {
        assertThat(transformer.transform(6)).isEqualTo("6 \"FOO\"");
    }

    @Test
    @DisplayName("Un nombre divisible par 5 doit retourner la ligne formatée avec BAR")
    void shouldReturnFormattedLineWithBarWhenDivisibleByFive() {
        assertThat(transformer.transform(10)).isEqualTo("10 \"BAR\"");
    }

    @Test
    @DisplayName("Un nombre divisible par 3 et 5 doit retourner la ligne formatée avec FOOBARFOO")
    void shouldReturnFormattedLineWithFooBarFooWhenDivisibleByThreeAndFive() {
        assertThat(transformer.transform(30)).isEqualTo("30 \"FOOBARFOO\"");
    }

    @Test
    @DisplayName("Un nombre contenant 3 doit retourner la ligne formatée avec FOO")
    void shouldReturnFormattedLineWithFooWhenContainsThree() {
        assertThat(transformer.transform(13)).isEqualTo("13 \"FOO\"");
    }

    @Test
    @DisplayName("Un nombre contenant 5 doit retourner la ligne formatée avec BAR")
    void shouldReturnFormattedLineWithBarWhenContainsFive() {
        assertThat(transformer.transform(52)).isEqualTo("52 \"BAR\"");
    }

    @Test
    @DisplayName("Un nombre contenant 7 doit retourner la ligne formatée avec QUIX")
    void shouldReturnFormattedLineWithQuixWhenContainsSeven() {
        assertThat(transformer.transform(17)).isEqualTo("17 \"QUIX\"");
    }

    @Test
    @DisplayName("33 : divisible par 3 + deux chiffres 3 doit retourner la ligne formatée FOOFOOFOO")
    void shouldReturnFormattedLineFor33() {
        assertThat(transformer.transform(33)).isEqualTo("33 \"FOOFOOFOO\"");
    }

    @Test
    @DisplayName("15 : divisible par 3 et 5 + chiffre 5 doit retourner la ligne formatée FOOBARBAR")
    void shouldReturnFormattedLineFor15() {
        assertThat(transformer.transform(15)).isEqualTo("15 \"FOOBARBAR\"");
    }

    @Test
    @DisplayName("37 : contient 3 et 7 doit retourner la ligne formatée FOOQUIX")
    void shouldReturnFormattedLineFor37() {
        assertThat(transformer.transform(37)).isEqualTo("37 \"FOOQUIX\"");
    }

    @Test
    @DisplayName("Un nombre négatif doit lever une InvalidNumberException")
    void shouldThrowWhenNumberIsNegative() {
        assertThatThrownBy(() -> transformer.transform(-1))
                .isInstanceOf(InvalidNumberException.class)
                .hasMessageContaining("-1");
    }

    @Test
    @DisplayName("Un nombre supérieur à 100 doit lever une InvalidNumberException")
    void shouldThrowWhenNumberIsAbove100() {
        assertThatThrownBy(() -> transformer.transform(101))
                .isInstanceOf(InvalidNumberException.class)
                .hasMessageContaining("101");
    }
}
