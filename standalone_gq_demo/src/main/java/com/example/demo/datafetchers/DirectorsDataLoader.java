package com.example.demo.datafetchers;

import com.example.demo.model.Director;
import com.example.demo.service.DirectorService;
import com.netflix.graphql.dgs.DgsDataLoader;
import org.dataloader.BatchLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "directors")
public class DirectorsDataLoader implements BatchLoader<String, Director> {

    private final DirectorService directorService;

    public DirectorsDataLoader(DirectorService directorService) {
        this.directorService = directorService;
    }

    @Override
    public CompletionStage<List<Director>> load(List<String> directorIds) {
        return CompletableFuture.supplyAsync(() -> directorService.findByIds(directorIds));
    }
}
