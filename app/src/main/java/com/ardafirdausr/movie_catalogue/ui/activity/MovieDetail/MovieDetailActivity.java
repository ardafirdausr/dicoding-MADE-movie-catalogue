package com.ardafirdausr.movie_catalogue.ui.activity.MovieDetail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView tvTitle, tvDescription, tvRating, tvReleaseDate;
    private ImageView ivPoster;
    private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setUpActionBar();
        bindView();
        initViewModel();
        renderExtraMovie();
    }

    private void initViewModel(){
        movieDetailViewModel = new ViewModelProvider(
                this,
                new MovieDetailViewModel.Factory(getApplication()))
                .get(MovieDetailViewModel.class);
    }

    private void setUpActionBar(){
        actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }


    private void bindView(){
        tvTitle = findViewById(R.id.tv_title);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvRating = findViewById(R.id.tv_rating);
        tvDescription = findViewById(R.id.tv_description);
        ivPoster = findViewById(R.id.iv_poster);
    }

    private void renderExtraMovie(){
        Bundle intentExtra = getIntent().getExtras();
        if(intentExtra != null){
            long movieId = MovieDetailActivityArgs.fromBundle(intentExtra).getMovieId();
            movieDetailViewModel.getMovie(movieId).observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(Movie movie) {
                    tvTitle.setText(movie.getTitle());
                    actionBar.setTitle(movie.getTitle());
                    tvReleaseDate.setText(movie.getReleaseDate());
                    tvRating.setText(Double.toString(movie.getVote()));
                    tvDescription.setText(movie.getDescription());
                    Picasso.get()
                            .load(movie.getImageUrl())
                            .resize(120, 160)
                            .transform(new RoundedCornersTransformation(10, 0))
                            .into(ivPoster);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
