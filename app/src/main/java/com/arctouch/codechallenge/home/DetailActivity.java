package com.arctouch.codechallenge.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Details;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;
import com.arctouch.codechallenge.presenters.MoviePresenter;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity {

    private  TextView titleTextView;
    private  TextView overviewTextView;
    private  TextView genresTextView;
    private  TextView releaseDateTextView;
    private  ImageView posterImageView;
    private  ImageView backdropImageView;
    private  MovieImageUrlBuilder movieImageUrlBuilder ;
    String posterPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        titleTextView = (TextView)findViewById(R.id.titleTextView);
        genresTextView = (TextView)findViewById(R.id.genresTextView);
        releaseDateTextView = (TextView)findViewById(R.id.releaseDateTextView);
        overviewTextView = (TextView)findViewById(R.id.overview);
        posterImageView = findViewById(R.id.posterImageView);
        backdropImageView = findViewById(R.id.backdropImageView);
        Movie movie= Cache.getMovies().get(Cache.getPosition());
        titleTextView.setText(movie.title);
        genresTextView.setText(TextUtils.join(", ", movie.genres));
        releaseDateTextView.setText(movie.releaseDate);
        overviewTextView.setText(movie.overview);
        movieImageUrlBuilder = new MovieImageUrlBuilder();

        posterPath = movie.posterPath;
        if (!TextUtils.isEmpty(posterPath)) {
            Glide.with(posterImageView)
                    .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(posterImageView);
        }
        posterPath = movie.backdropPath;
        if (!TextUtils.isEmpty(posterPath)) {
            Glide.with(backdropImageView)
                    .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(backdropImageView);
        }



    }
}
