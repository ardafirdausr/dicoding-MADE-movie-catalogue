package com.ardafirdausr.movie_catalogue.ui.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.ardafirdausr.movie_catalogue.receiver.AlarmReceiver;
import com.ardafirdausr.movie_catalogue.R;

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
        Log.d("WWWWW", languageCode);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch(preference.getKey()){
            case "language":
                changeLanguage(String.valueOf(newValue));
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
