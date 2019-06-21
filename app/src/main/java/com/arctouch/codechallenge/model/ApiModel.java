package com.arctouch.codechallenge.model;

import android.util.Log;
import android.view.View;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.contracts.MovieContract;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.home.HomeAdapter;
import com.arctouch.codechallenge.presenters.MoviePresenter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiModel implements MovieContract.Model {

    private MovieContract.Presenter moviePresenter;
    public ApiModel (MoviePresenter moviePresenter){
        this.moviePresenter=moviePresenter;
    }

    protected TmdbApi api = new Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi.class);

    @Override
    public void cache() {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                    moviePresenter.cacheComplete();
                });

    }

    @Override
    public void getUpcomingMovies() {

                    api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, Cache.getPage(), TmdbApi.DEFAULT_REGION)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response0 -> {
                                for (Movie movie : response0.results) {
                                    movie.genres = new ArrayList<>();
                                    for (Genre genre : Cache.getGenres()) {
                                        if (movie.genreIds.contains(genre.id)) {
                                            movie.genres.add(genre);
                                        }
                                    }
                                }
                                moviePresenter.handleMoviesList(response0);

                            });


    }

    @Override
    public void getSearch(String search) {

        api.searchMovies(TmdbApi.API_KEY,search, Cache.getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {Log.d("test1"," "+throwable.getMessage());})
                .subscribe(response0 -> {
                    for (Movie movie : response0.results) {
                        movie.genres = new ArrayList<>();
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.genreIds.contains(genre.id)) {
                                movie.genres.add(genre);
                            }
                        }
                    }
                    moviePresenter.handleMoviesList(response0);

                });




    }
}
