package com.arctouch.codechallenge.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.contracts.MovieContract;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Details;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;
import com.arctouch.codechallenge.presenters.MoviePresenter;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements MovieContract.View {

    // Index from which pagination should start (0 is 1st page in our case)
    private static final long PAGE_START = 0;

    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;

    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;

    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 10;
    // indicates the current page which Pagination is fetching.
    private long currentPage = PAGE_START;

    private HomeAdapter adapter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MoviePresenter moviePresenter;
    private LinearLayoutManager linearLayoutManager;
    private boolean firstLoad;
    private EditText editText;
    private Button search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        moviePresenter = new MoviePresenter(this);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);
        this.editText=findViewById(R.id.titleTextView);
        this.search=findViewById(R.id.search);
        firstLoad = true;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

// Setup Adapter
        recyclerView.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                progressBar.setVisibility(View.VISIBLE);
                isLoading = true;
                //Increment page index to load the next one
                currentPage += 1;
                Cache.setPage(currentPage);
                moviePresenter.cacheData(editText.getText().toString());
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage = 1;
                Cache.setPage(currentPage);
                moviePresenter.cacheData(editText.getText().toString());
            }
        });

        progressBar.setVisibility(View.GONE);
        if (Cache.getMovies().isEmpty()) {
            moviePresenter.cacheData(editText.getText().toString());
        } else {
            showMovieList(Cache.getMovies());
        }
    }

    @Override
    public void showMovieList(List<Movie> movie) {
        progressBar.setVisibility(View.GONE);
        if (firstLoad) {
            adapter = new HomeAdapter(movie, this);
            recyclerView.setAdapter(adapter);
        } else {
            firstLoad = false;
            loadNextPage(movie);
        }
    }

    @Override
    public void showDetails(Details details) {

    }

    @Override
    public void cacheComplete() {
        moviePresenter.returnMovieList();
    }

    private void loadNextPage(List<Movie> list) {
        adapter.removeLoadingFooter();
        isLoading = false;
        adapter.addAll(list);
        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }
}