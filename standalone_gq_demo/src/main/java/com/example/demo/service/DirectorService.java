package com.example.demo.service;

import com.example.demo.model.Director;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DirectorService {

    private final ConcurrentHashMap<String, Director> directors = new ConcurrentHashMap<>();

    public DirectorService() {
        directors.put("dir-1", new Director("dir-1", "Duffer Brothers"));
        directors.put("dir-2", new Director("dir-2", "Jason Bateman"));
        directors.put("dir-3", new Director("dir-3", "Peter Morgan"));
        directors.put("dir-4", new Director("dir-4", "Gavin Whitehead"));
    }

    public List<Director> findByIds(List<String> ids) {
        return ids.stream()
                  .map(directors::get)
                  .collect(Collectors.toList());
    }

    public Director findById(String id) {
        return directors.get(id);
    }
}
