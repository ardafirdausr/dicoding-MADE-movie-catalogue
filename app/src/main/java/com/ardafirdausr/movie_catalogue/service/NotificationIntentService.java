package com.ardafirdausr.movie_catalogue.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.ardafirdausr.movie_catalogue.BuildConfig;
import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.MovieApiInterface;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.MovieListResponse;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.MovieResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class NotificationIntentService extends IntentService {

    private MovieApiInterface movieApi;

    private static final String ACTION_SHOW_MOVIE_RELEASE_NOTIFICATION = "action_show_movie_release_notification";
    private static final String ACTION_SHOW_NOTIFICATION = "action_show_notification";

    private static final String EXTRA_NOTIFICATION_ID = "notification_id";
    private static final String EXTRA_NOTIFICATION_TITLE = "notification_title";
    private static final String EXTRA_NOTIFICATION_MESSAGE = "notification_message";

    public NotificationIntentService() {
        super("NotificationIntentService");
        movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
    }

    public static void startActionShowMovieReleaseNotification(Context context, String notificationTitle) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_SHOW_MOVIE_RELEASE_NOTIFICATION);
        intent.putExtra(EXTRA_NOTIFICATION_TITLE, notificationTitle);
        context.startService(intent);
    }

    public static void startActionShowNotification(
            Context context,
            int notificationId,
            String notificationTitle,
            String notificationMessage
    ) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_SHOW_NOTIFICATION);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        intent.putExtra(EXTRA_NOTIFICATION_TITLE, notificationTitle);
        intent.putExtra(EXTRA_NOTIFICATION_MESSAGE, notificationMessage);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_SHOW_NOTIFICATION.equals(action)){
                final int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0);
                final String title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
                final String message = intent.getStringExtra(EXTRA_NOTIFICATION_MESSAGE);
                handleActionShowNotification(notificationId, title, message);
            } else if(ACTION_SHOW_MOVIE_RELEASE_NOTIFICATION.equals(action)){
                final String title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
                handleActionShowMovieReleaseNotification(title);
            }
        }
    }


    private void handleActionShowNotification(int notificationId, String title, String message) {
        showNotification(notificationId, title, message, null);
    }

    private void handleActionShowMovieReleaseNotification(final String title) {
        final AtomicInteger c = new AtomicInteger(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String todayDateString = dateFormat.format(new Date());
        movieApi.getMovieRelease(
                BuildConfig.MOVIE_DB_API_KEY,
                "en-US",
                todayDateString,
                todayDateString)
            .enqueue(new Callback<MovieListResponse>() {

                @EverythingIsNonNull
                @Override
                public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                    if(response.body() != null){
                        List<MovieResponse> movies = response.body().getMovies();
                        for(MovieResponse movie: movies){
                            String message = movie.getTitle() + " " + getString(R.string.alarm_release_reminder_message);
                            showNotification(c.incrementAndGet(), title, message, movie.getId());
                        }
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<MovieListResponse> call, Throwable t) {
                    String message = t.getMessage() != null ? t.getMessage() : "Something went wrong";
                    Log.d("MovieCatalogue", message);
                }
            });
    }

    private void showNotification(int notificationId, String title, String message, @Nullable Long movieId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManagerCompat = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notificationId, notification);
        }

    }
}
