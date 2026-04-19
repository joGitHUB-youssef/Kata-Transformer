package com.example.springKata.transformer_ns.adapter.in.batch;

import com.example.springKata.transformer_ns.adapter.out.file.BatchInputReader;
import com.example.springKata.transformer_ns.adapter.out.file.BatchOutputWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile("batch")
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BatchInputReader reader;
    private final TransformerItemProcessor processor;
    private final BatchOutputWriter writer;
    private final TransformerSkipPolicy skipPolicy;
    private final TransformerStepListener listener;

    public BatchJobConfig(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          BatchInputReader reader,
                          TransformerItemProcessor processor,
                          BatchOutputWriter writer,
                          TransformerSkipPolicy skipPolicy,
                          TransformerStepListener listener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
        this.skipPolicy = skipPolicy;
        this.listener = listener;
    }

    @Bean
    public Job transformationJob() {
        return new JobBuilder("transformationJob", jobRepository)
                .start(transformationStep())
                .build();
    }

    @Bean
    public Step transformationStep() {
        return new StepBuilder("transformationStep", jobRepository)
                .<Integer, String>chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .listener(listener)
                .build();
    }
}
