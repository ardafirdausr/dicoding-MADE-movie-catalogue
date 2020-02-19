package com.ardafirdausr.movie_catalogue.ui.fragment.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.ui.activity.MainActivity;
import com.ardafirdausr.movie_catalogue.ui.adapter.FavouritePagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FavouritesFragment extends Fragment{

    public FavouritesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(view);
    }

    private void setupViewPager(View view){
        FavouritePagerAdapter favouritePagerAdapter = new FavouritePagerAdapter(
                getContext(),
                getChildFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(favouritePagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

}
