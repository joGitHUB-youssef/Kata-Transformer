package com.example.springKata.transformer_ns.adapter.out.file;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BatchInputReaderTest {

    private final ExecutionContext executionContext = new ExecutionContext();

    private BatchInputReader createReaderWithContent(String content) throws Exception {
        Resource resource = new ByteArrayResource(content.getBytes());
        BatchInputReader reader = new BatchInputReader(resource);
        reader.open(executionContext);
        return reader;
    }

    @Test
    @DisplayName("Le reader doit lire des nombres valides ligne par ligne")
    void shouldReadValidNumbers() throws Exception {
        BatchInputReader reader = createReaderWithContent("10\n20\n30\n");

        assertThat(reader.read()).isEqualTo(10);
        assertThat(reader.read()).isEqualTo(20);
        assertThat(reader.read()).isEqualTo(30);
        assertThat(reader.read()).isNull();

        reader.close();
    }

    @Test
    @DisplayName("Le reader doit retourner null quand le fichier est vide")
    void shouldReturnNullWhenFileIsEmpty() throws Exception {
        BatchInputReader reader = createReaderWithContent("");

        assertThat(reader.read()).isNull();

        reader.close();
    }

    @Test
    @DisplayName("Une ligne vide doit lever une IllegalArgumentException")
    void shouldThrowWhenLineIsBlank() throws Exception {
        BatchInputReader reader = createReaderWithContent("10\n\n30\n");

        assertThat(reader.read()).isEqualTo(10);
        assertThatThrownBy(reader::read)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vide");

        reader.close();
    }

    @Test
    @DisplayName("Un nombre hors plage (> 100) doit lever une InvalidNumberException")
    void shouldThrowWhenNumberIsOutOfRange() throws Exception {
        BatchInputReader reader = createReaderWithContent("10\n150\n30\n");

        assertThat(reader.read()).isEqualTo(10);
        assertThatThrownBy(reader::read)
                .isInstanceOf(InvalidNumberException.class)
                .hasMessageContaining("150");

        reader.close();
    }

    @Test
    @DisplayName("Une valeur non numérique doit lever une NumberFormatException")
    void shouldThrowWhenValueIsNotANumber() throws Exception {
        BatchInputReader reader = createReaderWithContent("10\nabc\n30\n");

        assertThat(reader.read()).isEqualTo(10);
        assertThatThrownBy(reader::read)
                .isInstanceOf(NumberFormatException.class)
                .hasMessageContaining("abc");

        reader.close();
    }
}
