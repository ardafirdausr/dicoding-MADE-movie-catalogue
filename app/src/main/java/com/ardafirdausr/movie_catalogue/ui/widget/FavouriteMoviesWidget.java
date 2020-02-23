package com.ardafirdausr.movie_catalogue.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.ui.activity.movieDetail.MovieDetailActivity;

public class FavouriteMoviesWidget extends AppWidgetProvider {

    private static final String OPEN_DETAIL_ACTION = "com.ardafirdausr.movie_catalogue.OPEN_DETAIL_ACTION";
    public static final String EXTRA_ITEM = "com.ardafirdausr.movie_catalogue.EXTRA_ITEM";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favourite_movies_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent openDetailIntent = new Intent(context, FavouriteMoviesWidget.class);
        openDetailIntent.setAction(FavouriteMoviesWidget.OPEN_DETAIL_ACTION);
        openDetailIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                openDetailIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(OPEN_DETAIL_ACTION)) {
                long selectedMovieId = intent.getLongExtra(EXTRA_ITEM, 0);
                Intent toDetailMovie = new Intent(context, MovieDetailActivity.class);
                toDetailMovie.addFlags(toDetailMovie.FLAG_ACTIVITY_NEW_TASK);
                toDetailMovie.addFlags(toDetailMovie.FLAG_ACTIVITY_CLEAR_TOP);
                toDetailMovie.putExtra("movieId", selectedMovieId);
                context.startActivity(toDetailMovie);
            }
        }
    }
}

