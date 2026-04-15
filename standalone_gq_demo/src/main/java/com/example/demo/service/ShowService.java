package com.example.demo.service;

import com.example.demo.model.Genre;
import com.example.demo.model.Show;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ShowService {

    private final ConcurrentHashMap<String, Show> shows = new ConcurrentHashMap<>();

    public ShowService() {
        createShow("Stranger Things", 2016, Genre.SCIFI, 8.7, "dir-1");
        createShow("Ozark", 2017, Genre.DRAMA, 8.5, "dir-2");
        createShow("The Crown", 2016, Genre.DRAMA, 8.6, "dir-3");
        createShow("Clarkson's Farm", 2021, Genre.COMEDY, 9.0, "dir-4");
    }

    public Show createShow(String title, int releaseYear, Genre genre, Double rating, String directorId) {
        String id = UUID.randomUUID().toString();
        Show show = new Show(id, title, releaseYear, genre, rating, directorId);
        shows.put(id, show);
        return show;
    }

    public List<Show> findAll() {
        return List.copyOf(shows.values());
    }

    public List<Show> findByTitleContaining(String titleFilter) {
        return shows.values().stream()
            .filter(show -> show.title().toLowerCase().contains(titleFilter.toLowerCase()))
            .collect(Collectors.toList());
    }

    public Optional<Show> findById(String id) {
        return Optional.ofNullable(shows.get(id));
    }

    public boolean deleteById(String id) {
        return shows.remove(id) != null;
    }
}
