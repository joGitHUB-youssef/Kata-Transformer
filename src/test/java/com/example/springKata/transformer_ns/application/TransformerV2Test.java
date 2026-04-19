package com.example.springKata.transformer_ns.application;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransformerV2Test {

    private final TransformerV2 transformer = new TransformerV2();

    @Test
    @DisplayName("Un nombre sans règle doit retourner le nombre en string")
    void shouldReturnNumberAsStringWhenNoRuleMatches() {
        assertThat(transformer.transform(4)).isEqualTo("4");
    }

    @Test
    @DisplayName("Un nombre divisible par 3 doit retourner FOO")
    void shouldReturnFooWhenDivisibleByThree() {
        assertThat(transformer.transform(6)).isEqualTo("FOO");
    }

    @Test
    @DisplayName("Un nombre divisible par 5 doit retourner BAR")
    void shouldReturnBarWhenDivisibleByFive() {
        assertThat(transformer.transform(10)).isEqualTo("BAR");
    }

    @Test
    @DisplayName("Un nombre divisible par 3 et 5 doit retourner FOOBAR")
    void shouldReturnFooBarWhenDivisibleByThreeAndFive() {
        assertThat(transformer.transform(30)).isEqualTo("FOOBARFOO");
    }

    @Test
    @DisplayName("Un nombre contenant 3 doit retourner FOO")
    void shouldReturnFooWhenContainsThree() {
        assertThat(transformer.transform(13)).isEqualTo("FOO");
    }

    @Test
    @DisplayName("Un nombre contenant 5 doit retourner BAR")
    void shouldReturnBarWhenContainsFive() {
        assertThat(transformer.transform(52)).isEqualTo("BAR");
    }

    @Test
    @DisplayName("Un nombre contenant 7 doit retourner QUIX")
    void shouldReturnQuixWhenContainsSeven() {
        assertThat(transformer.transform(17)).isEqualTo("QUIX");
    }

    @Test
    @DisplayName("33 : divisible par 3 + deux chiffres 3 doit retourner FOOFOOFOO")
    void shouldReturnFooFooFooFor33() {
        assertThat(transformer.transform(33)).isEqualTo("FOOFOOFOO");
    }

    @Test
    @DisplayName("15 : divisible par 3 et 5 + chiffre 5 doit retourner FOOBARBAR")
    void shouldReturnFooBarBarFor15() {
        assertThat(transformer.transform(15)).isEqualTo("FOOBARBAR");
    }

    @Test
    @DisplayName("37 : contient 3 et 7 doit retourner FOOQUIX")
    void shouldReturnFooQuixFor37() {
        assertThat(transformer.transform(37)).isEqualTo("FOOQUIX");
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
