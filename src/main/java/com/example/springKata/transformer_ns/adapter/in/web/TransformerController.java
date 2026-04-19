package com.example.springKata.transformer_ns.adapter.in.web;

import com.example.springKata.transformer_ns.domain.exception.InvalidNumberException;
import com.example.springKata.transformer_ns.domain.port.in.TransformerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transform")
public class TransformerController {

    private final TransformerService transformerService;

    public TransformerController(TransformerService transformerService) {
        this.transformerService = transformerService;
    }

    @GetMapping("/{number}")
    public ResponseEntity<String> transform(@PathVariable int number) {
        return ResponseEntity.ok(transformerService.transform(number));
    }

    @ExceptionHandler(InvalidNumberException.class)
    public ResponseEntity<String> handleInvalidNumber(InvalidNumberException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
