package com.example.springKata.transformer_ns.adapter.out.file;

import com.example.springKata.transformer_ns.domain.model.TransformResult;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
@Profile("batch")
public class BatchOutputWriter implements ItemWriter<TransformResult> {

    private final File outputFile;

    public BatchOutputWriter(@Value("${batch.output-file}") Resource outputResource) throws IOException {
        this.outputFile = outputResource.getFile();

        if (!outputFile.exists()) {
            if (!outputFile.createNewFile()) {
                throw new IOException("Impossible de créer le fichier de sortie : " + outputFile.getAbsolutePath());
            }
        } else {
            new FileWriter(outputFile, false).close();
        }
    }

    @Override
    public void write(Chunk<? extends TransformResult> items) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
            for (TransformResult item : items) {
                writer.write(item.getResult());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Erreur lors de l'écriture du fichier de sortie", e);
        }
    }
}
