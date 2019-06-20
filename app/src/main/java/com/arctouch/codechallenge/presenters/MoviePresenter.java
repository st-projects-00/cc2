package com.arctouch.codechallenge.presenters;

import com.arctouch.codechallenge.contracts.MovieContract;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.ApiModel;
import com.arctouch.codechallenge.model.Details;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

public class MoviePresenter implements MovieContract.Presenter {

    private MovieContract.View view;
    private ApiModel apiModel;

    public MoviePresenter(MovieContract.View view) {
        this.view = view;
        apiModel = new ApiModel(this);
    }

    @Override
    public void cacheData() {
        apiModel.cache();
    }

    @Override
    public void cacheComplete() {
        view.cacheComplete();
    }

    @Override
    public void returnMovieList() {
        apiModel.getUpcomingMovies();
    }

    @Override
    public void returnMovieDetail(String id) {

    }

    @Override
    public void handleMoviesList(UpcomingMoviesResponse upcomingMoviesResponse) {

        view.showMovieList(upcomingMoviesResponse);
    }

    @Override
    public void handleDetails(Details details) {

    }
}
