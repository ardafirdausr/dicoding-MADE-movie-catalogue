package com.ardafirdausr.movie_catalogue.ui.activity.tvShowDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TvShowDetailActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvTitle, tvDescription, tvRating, tvReleaseDate;
    private ImageView ivPoster, ivCover;
    private Toolbar toolbar;
    private FloatingActionButton fabFavourite;
    private TvShowDetailViewModel tvShowDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);
        bindView();
        setActionBar();
        initViewModel();
        renderExtraMovie();
    }

    private void bindView(){
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout = findViewById(R.id.app_bar);
        tvTitle = findViewById(R.id.tv_title);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvRating = findViewById(R.id.tv_rating);
        tvDescription = findViewById(R.id.tv_description);
        ivPoster = findViewById(R.id.iv_poster);
        ivCover = findViewById(R.id.iv_cover);
        toolbar = findViewById(R.id.toolbar);
        fabFavourite = findViewById(R.id.fab_favourite);
    }

    private void setActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViewModel(){
        tvShowDetailViewModel = new ViewModelProvider(
                this,
                new TvShowDetailViewModel.Factory(getApplication()))
                .get(TvShowDetailViewModel.class);
    }

    private void setActionBarTitle(final String title){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void renderExtraMovie(){
        Bundle intentExtra = getIntent().getExtras();
        if(intentExtra != null){
            long tvShowId = TvShowDetailActivityArgs.fromBundle(intentExtra).getTvShowId();
            tvShowDetailViewModel.getTvShow(tvShowId).observe(this, new Observer<TvShow>() {
                @Override
                public void onChanged(final TvShow tvShow) {
                    if(tvShow == null) return;
                    setActionBarTitle(tvShow.getTitle());
                    tvTitle.setText(tvShow.getTitle());
                    tvReleaseDate.setText(tvShow.getFirstAirDate());
                    tvRating.setText(Double.toString(tvShow.getVote()));
                    tvDescription.setText(tvShow.getDescription());
                    Picasso.get().load(tvShow.getCoverUrl()).into(ivCover);
                    Picasso.get()
                            .load(tvShow.getImageUrl())
                            .fit()
                            .centerCrop()
                            .transform(new RoundedCornersTransformation(10, 0))
                            .into(ivPoster);
                    int favouriteIconDrawableId = tvShow.getIsFavourite()
                            ? R.drawable.ic_favorite_brown_24dp
                            : R.drawable.ic_favorite_border_brown_24dp;
                    fabFavourite.setImageDrawable(getDrawable(favouriteIconDrawableId));
                    fabFavourite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(tvShow.getIsFavourite()){
                                tvShowDetailViewModel.removeTvShowFromFavourite(tvShow.getId());
                                Toast.makeText(
                                        getApplicationContext(),
                                        R.string.success_remove_from_favourite,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                            else {
                                tvShowDetailViewModel.addTvShowToFavourite(tvShow.getId());
                                Toast.makeText(
                                        getApplicationContext(),
                                        R.string.success_add_to_favourite,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
