package com.ardafirdausr.movie_catalogue.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.api.movie.response.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvRating;
    private ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        bindView();
        renderExtraMovie();
    }

    private void bindView(){
        tvTitle = findViewById(R.id.tv_title);
        tvRating = findViewById(R.id.tv_rating);
        tvDescription = findViewById(R.id.tv_description);
        ivPoster = findViewById(R.id.iv_poster);
    }

    private void renderExtraMovie(){
        Bundle intentExtra = getIntent().getExtras();
        if(intentExtra != null){
            Movie movie = MovieDetailActivityArgs.fromBundle(intentExtra).getMovie();
            tvTitle.setText(movie.getTitle());
            tvRating.setText(movie.getVote() + " / " + "10");
            tvDescription.setText(movie.getDescription());
            Picasso.get().load(movie.getImageUrl()).into(ivPoster);
        }
    }

}
