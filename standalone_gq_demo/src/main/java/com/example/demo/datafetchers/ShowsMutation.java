package com.example.demo.datafetchers;

import com.example.demo.model.Show;
import com.example.demo.model.SubmittedShow;
import com.example.demo.service.ShowService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;

@DgsComponent
public class ShowsMutation {

    private final ShowService showService;

    public ShowsMutation(ShowService showService) {
        this.showService = showService;
    }

    @DgsMutation
    public Show addShow(@InputArgument SubmittedShow show) {
        return showService.createShow(
            show.title(),
            show.releaseYear(),
            show.genre(),
            null, 
            show.directorId()
        );
    }

    @DgsMutation
    public boolean deleteShow(@InputArgument String id) {
        return showService.deleteById(id);
    }
}
