package com.example.springKata.transformer_ns.domain.model;

public class TransformResult {

    private final int number;
    private final String result;

    public TransformResult(int number, String result) {
        this.number = number;
        this.result = result;
    }

    public int getNumber() {
        return number;
    }

    public String getResult() {
        return result;
    }
}
