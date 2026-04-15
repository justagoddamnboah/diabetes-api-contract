package com.example.demo.model;

public record SubmittedShow(
    String title,
    int releaseYear,
    Genre genre,
    String directorId
) {}
