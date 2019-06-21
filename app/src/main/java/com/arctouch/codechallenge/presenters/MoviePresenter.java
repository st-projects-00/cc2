package com.arctouch.codechallenge.presenters;

import android.util.Log;

import com.arctouch.codechallenge.contracts.MovieContract;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.ApiModel;
import com.arctouch.codechallenge.model.Details;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

public class MoviePresenter implements MovieContract.Presenter {

    private MovieContract.View view;
    private ApiModel apiModel;
    private String search;

    public MoviePresenter(MovieContract.View view) {
        this.view = view;
        apiModel = new ApiModel(this);
    }

    @Override
    public void cacheData(String search) {
        this.search=search;
        apiModel.cache();
    }

    @Override
    public void cacheComplete() {
        view.cacheComplete();
    }

    @Override
    public void returnMovieList() {
        if (search.isEmpty()){
        apiModel.getUpcomingMovies();}
        else {
            apiModel.getSearch(search);
        }
    }

    @Override
    public void returnMovieDetail(String id) {

    }

    @Override
    public void handleMoviesList(UpcomingMoviesResponse upcomingMoviesResponse) {
        Cache.setMovies(upcomingMoviesResponse.results);
        Log.d("test1","data");
        view.showMovieList(upcomingMoviesResponse.results);
    }

    @Override
    public void handleDetails(Details details) {

    }
}
