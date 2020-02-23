package com.ardafirdausr.movie_catalogue.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.ardafirdausr.movie_catalogue.receiver.AlarmReceiver;
import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.ui.activity.MainActivity;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener{

    private AlarmReceiver alarmReceiver;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey);
        initAlarmReceiver();
        initPreferences();
    }

    private void initAlarmReceiver(){
        alarmReceiver = new AlarmReceiver(getContext());
    }

    private void initPreferences(){
        final ListPreference localeListPreference = findPreference("language");
        if(localeListPreference != null) {
            localeListPreference.setOnPreferenceChangeListener(this);
        }
        final SwitchPreferenceCompat dailyReminderSwitchPreference = findPreference("daily_reminder");
        if(dailyReminderSwitchPreference != null){
            dailyReminderSwitchPreference.setOnPreferenceChangeListener(this);
        }
        final SwitchPreferenceCompat releaseReminderSwitchPreference = findPreference("release_reminder");
        if(releaseReminderSwitchPreference != null){
            releaseReminderSwitchPreference.setOnPreferenceChangeListener(this);
        }
    }

    private void changeLanguage(String languageCode){
        String[] locale = languageCode.split("-");
        String language = locale[0];
        String countryCode = locale[1];
        Locale newLocale = new Locale(language, countryCode);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(newLocale);
        res.updateConfiguration(conf, dm);

        // refresh activity
        Intent refresh = new Intent(getContext(), MainActivity.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(refresh);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch(preference.getKey()){
            case "language":
                changeLanguage((String) newValue);
                break;
            case "daily_reminder":
                if((boolean) newValue){
                    alarmReceiver.setDailyReminderAlarm(
                            "07:00",
                            getContext().getString(R.string.alarm_daily_reminder_title),
                            getContext().getString(R.string.alarm_daily_reminder_message));
                } else {
                    alarmReceiver.cancelDailyAlarm();
                }
                break;
            case "release_reminder":
                if((boolean) newValue){
                    alarmReceiver.setReleaseReminder(
                            "08:00",
                            getContext().getString(R.string.alarm_release_reminder_title));
                } else {
                    alarmReceiver.cancelReleaseAlarm();
                }
                break;
        }
        return true;
    }

}
