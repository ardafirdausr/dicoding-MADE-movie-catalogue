package com.ardafirdausr.movie_catalogue.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.ui.fragment.favourite.FavouriteMoviesFragment;
import com.ardafirdausr.movie_catalogue.ui.fragment.favourite.FavouriteTvShowsFragment;

public class FavouritePagerAdapter extends FragmentPagerAdapter {

    Context context;

    public FavouritePagerAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.context = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.title_movies,
            R.string.title_tv_shows
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FavouriteMoviesFragment();
                break;
            case 1:
                fragment = new FavouriteTvShowsFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

}
