package com.example.demo.model;

public record Show(
    String id,
    String title,
    int releaseYear,
    Genre genre,
    Double rating,
    String directorId

) {}
