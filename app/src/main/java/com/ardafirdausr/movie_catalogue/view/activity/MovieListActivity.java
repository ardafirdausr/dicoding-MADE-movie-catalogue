package com.ardafirdausr.movie_catalogue.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.ardafirdausr.movie_catalogue.BuildConfig;
import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiInterface;
import com.ardafirdausr.movie_catalogue.api.movie.response.Movie;
import com.ardafirdausr.movie_catalogue.api.movie.response.MovieList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {

    private MovieApiInterface movieApi;
    private ListView movieListView;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        bindView();
        fetchNowPlayingMovies();
    }

    private void bindView(){
        this.movieListView = findViewById(R.id.lv_movie_list);
    }

    private void fetchNowPlayingMovies(){
        movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
        Call<MovieList> movieListRequest = movieApi.getNowPlayingMovies(BuildConfig.MOVIE_DB_API_KEY, 1);
        movieListRequest.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if(response.isSuccessful()){
                    Log.d("FETCH PLAYING MOVIES", "SUCCESS");
                    movies = response.body().getMovies();
                }
                else {
                    Log.d("FETCH PLAYING MOVIES", "FAILED CODE : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.e("FETCH PLAYING MOVIES", t.getMessage());
            }
        });
    }

}
