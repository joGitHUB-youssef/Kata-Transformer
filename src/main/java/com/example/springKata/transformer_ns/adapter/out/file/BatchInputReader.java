package com.example.springKata.transformer_ns.adapter.out.file;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@Profile("batch")
public class BatchInputReader implements ItemStreamReader<Integer> {

    private final Resource inputResource;
    private BufferedReader reader;

    public BatchInputReader(@Value("${batch.input-file}") Resource inputResource) {
        this.inputResource = inputResource;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        if (!inputResource.exists()) {
            throw new IllegalStateException("Fichier introuvable : " + inputResource.getFilename());
        }
        if (!inputResource.isReadable()) {
            throw new IllegalStateException("Fichier non lisible : " + inputResource.getFilename());
        }
        try {
            this.reader = new BufferedReader(new InputStreamReader(inputResource.getInputStream()));
        } catch (IOException e) {
            throw new IllegalStateException("Impossible d'ouvrir le fichier", e);
        }
    }

    @Override
    public Integer read() throws Exception {
        String line = reader.readLine();

        if (line == null) return null;

        if (line.isBlank()) {
            throw new IllegalArgumentException("Ligne vide dans le fichier d'entrée");
        }

        try {
            int number = Integer.parseInt(line.trim());
            if (number < 0 || number > 100) {
                throw new InvalidNumberException(number);
            }
            return number;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Valeur non numérique : " + line.trim());
        }
    }

    @Override
    public void close() {
        try {
            if (reader != null) reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Erreur à la fermeture du fichier", e);
        }
    }
}
