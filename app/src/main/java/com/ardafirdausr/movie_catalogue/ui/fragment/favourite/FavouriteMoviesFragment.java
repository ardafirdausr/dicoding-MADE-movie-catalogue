package com.ardafirdausr.movie_catalogue.ui.fragment.favourite;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.ardafirdausr.movie_catalogue.ui.activity.MovieDetail.MovieDetailActivity;
import com.ardafirdausr.movie_catalogue.ui.adapter.MovieAdapter;
import com.ardafirdausr.movie_catalogue.ui.fragment.movie.MoviesFragmentDirections;

import java.util.List;

public class FavouriteMoviesFragment extends Fragment
    implements LifecycleOwner {

    private RecyclerView rvFavouriteMovies;
    private FavouriteMoviesViewModel favouriteMoviesViewModel;

    public FavouriteMoviesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initViewModel();
    }

    private void bindView(View view){
        rvFavouriteMovies = view.findViewById(R.id.rv_movie_list);
    }

    private void initViewModel(){
        Application application = null;
        if(getActivity() != null) application = getActivity().getApplication();
        if(application != null){
            favouriteMoviesViewModel = new ViewModelProvider(
                    this,
                    new FavouriteMoviesViewModel.Factory(application))
                    .get(FavouriteMoviesViewModel.class);
            registerObserver();
        }
    }

    private void registerObserver(){
        observeFavouriteMovies();
    }

    private void observeFavouriteMovies(){
        favouriteMoviesViewModel.getFavouriteMovies().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movieResponses) {
                renderMovieList(movieResponses);
            }
        });
    }

    private void renderMovieList(List<Movie> movies){
        MovieAdapter movieAdapter = new MovieAdapter();
        movieAdapter.setMovie(movies);
        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, Movie movie) {
                Intent toMovieDetailActivity = new Intent(getContext(), MovieDetailActivity.class);
                toMovieDetailActivity.putExtra("movieId", movie.getId());
                startActivity(toMovieDetailActivity);
//                FavouriteMoviesFragmentDirections.ActionFavouriteMoviesFragmentToMovieDetailActivity
//                        toMovieDetailActivity = FavouriteMoviesFragmentDirections
//                            .actionFavouriteMoviesFragmentToMovieDetailActivity(movie.getId());
//                toMovieDetailActivity.setMovieId(movie.getId());
//                Navigation.findNavController(view)
//                        .navigate(toMovieDetailActivity);

            }
        });
        rvFavouriteMovies.setAdapter(movieAdapter);
        rvFavouriteMovies.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
