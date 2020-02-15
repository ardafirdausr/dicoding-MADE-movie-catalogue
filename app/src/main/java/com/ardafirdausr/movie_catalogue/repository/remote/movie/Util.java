package com.ardafirdausr.movie_catalogue.repository.remote.movie;

import com.ardafirdausr.movie_catalogue.BuildConfig;

import java.util.Locale;

public class Util {

    public static String getCurrentLanguage(){
        String countryCode = Locale.getDefault().getCountry();
        String languageCode = Locale.getDefault().getLanguage();
        languageCode = languageCode.equals("in") ? "id" : languageCode;
        return languageCode + "-" + countryCode;
    }

    public static String getApiKey(){
        return BuildConfig.MOVIE_DB_API_KEY;
    }

}
