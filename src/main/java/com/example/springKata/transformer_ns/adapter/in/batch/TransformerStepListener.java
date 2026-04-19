package com.example.springKata.transformer_ns.adapter.in.batch;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.stereotype.Component;

@Component
public class TransformerStepListener implements StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerStepListener.class);

    private int invalidNumbers = 0;
    private int outOfRangeNumbers = 0;
    private int invalidFormat = 0;

    @OnSkipInRead
    public void onSkipInRead(Throwable t) {
        if (t instanceof InvalidNumberException) {
            outOfRangeNumbers++;
        } else if (t instanceof NumberFormatException) {
            invalidFormat++;
        } else if (t instanceof IllegalArgumentException) {
            invalidNumbers++;
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("--- Rapport batch ---");
        LOGGER.info("- Lignes traitées   : {}", stepExecution.getWriteCount());
        LOGGER.info("- Hors plage (0-100): {}", outOfRangeNumbers);
        LOGGER.info("- Format invalide   : {}", invalidFormat);
        LOGGER.info("- Lignes vides      : {}", invalidNumbers);
        LOGGER.info("- Total skippés     : {}", stepExecution.getSkipCount());
        return stepExecution.getExitStatus();
    }
}
