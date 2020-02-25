package com.ardafirdausr.favourite.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardafirdausr.favourite.R;
import com.ardafirdausr.favourite.entity.Movie;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvTitle, tvDescription, tvRating, tvReleaseDate;
    private ImageView ivPoster, ivCover;
    private Toolbar toolbar;
    private FloatingActionButton fabFavourite;

    public final String EXTRA_MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        bindView();
        setActionBar();
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

//    private void updateFavouriteWidget(){
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//        int[] movieCatalogueWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
//                getPackageName(),
//                FavouriteMoviesWidget.class.getName()));
//        if(movieCatalogueWidgetIds.length > 0){
//            appWidgetManager.notifyAppWidgetViewDataChanged(movieCatalogueWidgetIds, R.id.stack_view);
//        }
//    }

    private void renderExtraMovie(){
        final Bundle intentExtra = getIntent().getExtras();
        if(intentExtra != null){
            final Movie movie = intentExtra.getParcelable(EXTRA_MOVIE);
            if(movie != null){
                setActionBarTitle(movie.getTitle());
                tvTitle.setText(movie.getTitle());
                tvReleaseDate.setText(movie.getReleaseDate());
                tvRating.setText(Double.toString(movie.getVote()));
                tvDescription.setText(movie.getDescription());
                Picasso.get().load(movie.getCoverUrl()).into(ivCover);
                Picasso.get()
                        .load(movie.getImageUrl())
                        .fit()
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(10, 0))
                        .into(ivPoster);
                int favouriteIconDrawableId = movie.getIsFavourite()
                        ? R.drawable.ic_favorite_brown_24dp
                        : R.drawable.ic_favorite_border_brown_24dp;
                fabFavourite.setImageDrawable(getDrawable(favouriteIconDrawableId));
                fabFavourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if(movie.getIsFavourite()){
//                            movieDetailViewModel.removeMovieFromFavourite(movie.getId());
//                            Toast.makeText(
//                                    getApplicationContext(),
//                                    R.string.success_remove_from_favourite,
//                                    Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                        else {
//                            movieDetailViewModel.addMovieToFavourite(movie.getId());
//                            Toast.makeText(
//                                    getApplicationContext(),
//                                    R.string.success_add_to_favourite,
//                                    Toast.LENGTH_SHORT)
//                                    .show();
//                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
