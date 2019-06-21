package com.arctouch.codechallenge.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.contracts.MovieContract;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<Movie> movies;
    private boolean isLoadingAdded = false;
    private MovieContract.View view;
    private final HomeActivity homeActivity;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(homeActivity, DetailActivity.class);
            homeActivity.startActivity(intent);
        }
    };

    public HomeAdapter(List<Movie> movies, HomeActivity homeActivity) {
        this.movies = movies;
        this.homeActivity = homeActivity;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView releaseDateTextView;
        private final ImageView posterImageView;
        private HomeActivity homeActivity;


        public ViewHolder(View itemView, HomeActivity homeActivity) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genresTextView = itemView.findViewById(R.id.genresTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            this.homeActivity = homeActivity;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Cache.setPosition(getAdapterPosition());
            Intent intent = new Intent(homeActivity, DetailActivity.class);
            homeActivity.startActivity(intent);

        }

        public void bind(Movie movie) {
            if (movie != null) {
                titleTextView.setText(movie.title);
                Log.d("test1", "movie" + movie.title);
                if (movie.genres != null) {
                    genresTextView.setText(TextUtils.join(", ", movie.genres));
                }
                releaseDateTextView.setText(movie.releaseDate);
                String posterPath = movie.posterPath;
                if (TextUtils.isEmpty(posterPath) == false) {
                    Glide.with(itemView)
                            .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                            .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                            .into(posterImageView);
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view, homeActivity);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movies.get(position));
        //  holder.itemView.setOnClickListener(mOnClickListener);
    }

    public void add(Movie mc) {
        movies.add(mc);
        notifyItemInserted(movies.size() - 1);
    }

    public void addAll(List<Movie> mcList) {
        for (Movie mc : mcList) {
            add(mc);
        }
    }

    public void remove(Movie city) {
        int position = movies.indexOf(city);
        if (position > -1) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movies.size() - 1;
        Movie item = getItem(position);
        if (item != null) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }

}
