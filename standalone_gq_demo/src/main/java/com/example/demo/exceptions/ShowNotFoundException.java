package com.example.demo.exceptions;

public class ShowNotFoundException extends RuntimeException {
    public ShowNotFoundException(String id) {
        super("Show not found: " + id);
    }
}
