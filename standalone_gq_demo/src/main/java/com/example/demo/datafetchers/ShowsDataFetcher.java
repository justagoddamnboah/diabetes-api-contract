package com.example.demo.datafetchers;

import com.example.demo.exceptions.ShowNotFoundException;
import com.example.demo.model.Director;
import com.example.demo.model.Show;
import com.example.demo.service.ShowService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
public class ShowsDataFetcher {

    private final ShowService showService;

    public ShowsDataFetcher(ShowService showService) {
        this.showService = showService;
    }

    @DgsQuery
    public List<Show> shows(@InputArgument String titleFilter) {
        if (titleFilter == null) {
            return showService.findAll();
        }
        return showService.findByTitleContaining(titleFilter);
    }

    @DgsQuery
    public Show show(@InputArgument String id) {
        return showService.findById(id).orElseThrow(() -> new ShowNotFoundException(id));
    }

    @DgsData(parentType = "Show", field = "director")
    public CompletableFuture<Director> director(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, Director> loader = dfe.getDataLoader("directors");
        Show show = dfe.getSource();
        if (show.directorId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return loader.load(show.directorId());
    }
}
