package com.ardafirdausr.movie_catalogue.ui.activity;

import android.os.Bundle;
import android.view.Menu;

import com.ardafirdausr.movie_catalogue.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_movies,
                R.id.navigation_tv_shows,
                R.id.navigation_favourites,
                R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        if(getSupportActionBar() != null){
            getSupportActionBar().setElevation(0);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_change_settings) {
////            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
////            startActivity(mIntent);
//            Intent toSettingActivity = new Intent(this, SettingActivity.class);
//            startActivity(toSettingActivity);
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
