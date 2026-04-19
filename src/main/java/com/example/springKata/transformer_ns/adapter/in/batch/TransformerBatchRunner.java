package com.example.springKata.transformer_ns.adapter.in.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("batch")
public class TransformerBatchRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerBatchRunner.class);

    private final JobLauncher jobLauncher;
    private final Job transformationJob;

    public TransformerBatchRunner(JobLauncher jobLauncher, Job transformationJob) {
        this.jobLauncher = jobLauncher;
        this.transformationJob = transformationJob;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Démarrage du batch...");

        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(transformationJob, params);
    }
}
