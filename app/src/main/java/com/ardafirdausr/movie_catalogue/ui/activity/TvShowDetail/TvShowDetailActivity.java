package com.ardafirdausr.movie_catalogue.ui.activity.TvShowDetail;

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
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TvShowDetailActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView tvTitle, tvDescription, tvRating, tvReleaseDate;
    private ImageView ivPoster;
    private TvShowDetailViewModel tvShowDetailViewModelViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);
        setUpActionBar();
        bindView();
        initViewModel();
        renderExtraMovie();
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

    private void initViewModel(){
        tvShowDetailViewModelViewModel = new ViewModelProvider(
                this,
                new TvShowDetailViewModel.Factory(getApplication()))
            .get(TvShowDetailViewModel.class);
    }

    private void renderExtraMovie(){
        Bundle intentExtra = getIntent().getExtras();
        if(intentExtra != null){
            long tvShowId = TvShowDetailActivityArgs.fromBundle(intentExtra).getTvShowId();
            tvShowDetailViewModelViewModel.getTvShow(tvShowId).observe(this, new Observer<TvShow>() {
                @Override
                public void onChanged(TvShow tvShow) {
                    tvTitle.setText(tvShow.getTitle());
                    actionBar.setTitle(tvShow.getTitle());
                    tvReleaseDate.setText(tvShow.getFirstAirDate());
                    tvRating.setText(Double.toString(tvShow.getVote()));
                    tvDescription.setText(tvShow.getDescription());
                    Picasso.get()
                            .load(tvShow.getImageUrl())
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
