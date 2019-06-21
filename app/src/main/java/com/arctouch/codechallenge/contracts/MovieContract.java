package com.arctouch.codechallenge.contracts;

import com.arctouch.codechallenge.model.Details;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import java.util.List;

public class MovieContract {

        public interface Presenter{
          //from view
           void returnMovieList();
           void returnMovieDetail(String id) ;
           void cacheData(String search);
           //from model
            void handleMoviesList(UpcomingMoviesResponse upcomingMoviesResponse);
            void handleDetails(Details details);
            void cacheComplete();
        }

        public interface View {
           void showMovieList(List<Movie> movie);
           void showDetails(Details details);
            void cacheComplete();
        }

    public interface Model {
        void cache();
        void getUpcomingMovies();
        void getSearch(String search);
    }
    }

