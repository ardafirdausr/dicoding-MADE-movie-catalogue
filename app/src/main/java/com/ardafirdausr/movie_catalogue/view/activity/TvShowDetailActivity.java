package com.ardafirdausr.movie_catalogue.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.api.movie.response.TvShow;
import com.squareup.picasso.Picasso;

public class TvShowDetailActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView tvTitle, tvDescription, tvRating, tvReleaseDate;
    private ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);
        setUpActionBar();
        bindView();
        renderExtraMovie();
    }

    private void setUpActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            TvShow tvShow = TvShowDetailActivityArgs.fromBundle(intentExtra).getTvShow();
            tvTitle.setText(tvShow.getTitle());
            actionBar.setTitle(tvShow.getTitle());
            tvReleaseDate.setText(tvShow.getFirstAirDate());
            tvRating.setText(Double.toString(tvShow.getVote()));
            tvDescription.setText(tvShow.getDescription());
            Picasso.get().load(tvShow.getImageUrl()).into(ivPoster);
        }
    }
}
