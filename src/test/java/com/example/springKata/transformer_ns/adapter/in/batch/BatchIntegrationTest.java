package com.example.springKata.transformer_ns.adapter.in.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("batch")
class BatchIntegrationTest {

    @TempDir
    static Path tempDir;

    static Path outputFilePath;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) throws IOException {
        Path inputFile = tempDir.resolve("input.txt");
        outputFilePath = tempDir.resolve("output.txt");

        Files.writeString(inputFile, "10\n33\n15\n7\nabc\n150\n4\n");

        registry.add("batch.input-file", () -> "file:" + inputFile.toAbsolutePath());
        registry.add("batch.output-file", () -> "file:" + outputFilePath.toAbsolutePath());
    }

    @BeforeEach
    void clearOutputFile() throws IOException {
        Files.writeString(outputFilePath, "");
    }

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    @DisplayName("[IT] Le job batch doit se terminer avec le statut COMPLETED")
    void shouldCompleteSuccessfully() throws Exception {
        JobExecution execution = jobLauncherTestUtils.launchJob();

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("[IT] Le job batch doit écrire les résultats transformés dans output.txt")
    void shouldWriteTransformedResultsToOutputFile() throws Exception {
        jobLauncherTestUtils.launchJob();

        Path outputFile = tempDir.resolve("output.txt");
        List<String> lines = Files.readAllLines(outputFile);

        assertThat(lines).containsExactly(
                "10 \"BAR\"",
                "33 \"FOOFOOFOO\"",
                "15 \"FOOBARBAR\"",
                "7 \"QUIX\"",
                "4 \"4\""
        );
    }

    @Test
    @DisplayName("[IT] Le job batch doit skipper les lignes invalides")
    void shouldSkipInvalidLines() throws Exception {
        JobExecution execution = jobLauncherTestUtils.launchJob();

        assertThat(execution.getStepExecutions())
                .first()
                .satisfies(step -> {
                    assertThat(step.getWriteCount()).isEqualTo(5);
                    assertThat(step.getSkipCount()).isEqualTo(2);
                });
    }
}
