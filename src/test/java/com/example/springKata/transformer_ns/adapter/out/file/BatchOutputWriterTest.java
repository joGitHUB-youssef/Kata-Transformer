package com.example.springKata.transformer_ns.adapter.out.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BatchOutputWriterTest {

    private File tempFile;
    private BatchOutputWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("test-output", ".txt");
        tempFile.deleteOnExit();

        Resource resource = mock(Resource.class);
        when(resource.getFile()).thenReturn(tempFile);

        writer = new BatchOutputWriter(resource);
    }

    @Test
    @DisplayName("Le writer doit écrire les résultats ligne par ligne dans le fichier")
    void shouldWriteItemsToFile() throws Exception {
        writer.write(Chunk.of("FOO", "BAR", "4"));

        List<String> lines = Files.readAllLines(tempFile.toPath());

        assertThat(lines).hasSize(3);
        assertThat(lines.get(0)).isEqualTo("FOO");
        assertThat(lines.get(1)).isEqualTo("BAR");
        assertThat(lines.get(2)).isEqualTo("4");
    }

    @Test
    @DisplayName("Le writer doit écrire en append sur plusieurs chunks")
    void shouldAppendOnMultipleChunks() throws Exception {
        writer.write(Chunk.of("FOO", "BAR"));
        writer.write(Chunk.of("FOOFOOFOO"));

        List<String> lines = Files.readAllLines(tempFile.toPath());

        assertThat(lines).hasSize(3);
        assertThat(lines.get(0)).isEqualTo("FOO");
        assertThat(lines.get(1)).isEqualTo("BAR");
        assertThat(lines.get(2)).isEqualTo("FOOFOOFOO");
    }

    @Test
    @DisplayName("Le writer doit gérer un chunk vide sans erreur")
    void shouldHandleEmptyChunk() throws Exception {
        writer.write(Chunk.of());

        List<String> lines = Files.readAllLines(tempFile.toPath());

        assertThat(lines).isEmpty();
    }
}
