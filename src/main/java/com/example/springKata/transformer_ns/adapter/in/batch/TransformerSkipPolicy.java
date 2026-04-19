package com.example.springKata.transformer_ns.adapter.in.batch;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("batch")
public class TransformerSkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        return t instanceof InvalidNumberException
            || t instanceof NumberFormatException
            || t instanceof IllegalArgumentException;
    }
}
