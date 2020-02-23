package com.ardafirdausr.movie_catalogue.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.MovieCatalogueDatabase;
import com.ardafirdausr.movie_catalogue.repository.local.dao.MovieDao;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<Bitmap> widgetItems = new ArrayList<>();
    private final List<Movie> moviesData = new ArrayList<>();
    private final Context context;
    private final MovieDao movieDao;

    StackRemoteViewsFactory(Context context) {
        this.context = context;
        movieDao = MovieCatalogueDatabase.getInstance(context).getMovieDao();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        widgetItems.clear();
        moviesData.clear();
        List<Movie> movies = movieDao.getFavouriteMoviesData();
        for(Movie movie: movies){
            try{
                Bitmap widgetItemImage = Picasso.get()
                        .load(movie.getImageUrl())
                        .get();
                widgetItems.add(widgetItemImage);
                moviesData.add(movie);
            } catch (Exception e){
                String errorMessage = e.getMessage() != null ? e.getMessage() : "Error get bitmap";
                Log.e("MovieCatalogue", errorMessage);
            }

        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return widgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.favourite_movie_widget_item);
        rv.setImageViewBitmap(R.id.imageView, widgetItems.get(position));
        Bundle extras = new Bundle();
        extras.putLong(FavouriteMoviesWidget.EXTRA_ITEM, moviesData.get(position).getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
