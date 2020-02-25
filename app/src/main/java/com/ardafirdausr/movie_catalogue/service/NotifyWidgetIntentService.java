package com.ardafirdausr.movie_catalogue.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.ui.widget.FavouriteMoviesWidget;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotifyWidgetIntentService extends IntentService {

    private static final String ACTION_NOTIFY_FAVOURITE_WIDGET = "action_notify_favourite_widget";

    public NotifyWidgetIntentService() { super("NotifyWidgetIntentService"); }

    public static void startActionNotifyFavouriteWidget(Context context) {
        Intent intent = new Intent(context, NotifyWidgetIntentService.class);
        intent.setAction(ACTION_NOTIFY_FAVOURITE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFY_FAVOURITE_WIDGET.equals(action)) {
                handleNotifyFavouriteWidget();
            }
        }
    }

    private void handleNotifyFavouriteWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] movieCatalogueWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
                getPackageName(),
                FavouriteMoviesWidget.class.getName()));
        if(movieCatalogueWidgetIds.length > 0){
            appWidgetManager.notifyAppWidgetViewDataChanged(movieCatalogueWidgetIds, R.id.stack_view);
        }
    }

}
