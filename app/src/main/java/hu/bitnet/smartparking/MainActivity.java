package hu.bitnet.smartparking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import hu.bitnet.smartparking.Fragments.History;
import hu.bitnet.smartparking.Fragments.Home;
import hu.bitnet.smartparking.Fragments.Login;
import hu.bitnet.smartparking.Fragments.Profile;
import hu.bitnet.smartparking.Fragments.Settings;
import hu.bitnet.smartparking.Objects.Constants;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    BottomNavigationView bottomNavigationView;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getPreferences(0);
        //start();

        if (preferences.getBoolean(Constants.IS_LOGGED_IN, true)) {
            setContentView(R.layout.activity_main);
        } else {
            Login login = new Login();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainframe, login, login.getTag())
                    .commit();
        }


        final BottomNavigationItemView parking = (BottomNavigationItemView) findViewById(R.id.action_parking);
        final BottomNavigationItemView history = (BottomNavigationItemView) findViewById(R.id.action_history);
        final BottomNavigationItemView profile = (BottomNavigationItemView) findViewById(R.id.action_profile);
        final BottomNavigationItemView settings = (BottomNavigationItemView) findViewById(R.id.action_settings);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navbar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.appbar);
        relativeLayout.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_parking) {
                    item.setChecked(item.getItemId() == 0);
                    index = getSupportFragmentManager().getBackStackEntryCount();
                    if (index == 0) {
                        Home home = new Home();
                        FragmentManager fragmentManager0 = getSupportFragmentManager();
                        fragmentManager0.beginTransaction()
                                .replace(R.id.frame, home, "Home")
                                .commit();
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                } else {
                    switch (item.getItemId()) {
                        case R.id.action_history:
                            item.setChecked(item.getItemId() == 1);
                            History history1 = new History();
                            FragmentManager fragmentManager1 = getSupportFragmentManager();
                            index = fragmentManager1.getBackStackEntryCount();
                            if (index == 0) {
                                fragmentManager1.beginTransaction()
                                        .replace(R.id.frame, history1, history1.getTag())
                                        .addToBackStack("History")
                                        .commit();
                            } else {
                                index = index - 1;
                                FragmentManager.BackStackEntry backStackEntry1 = fragmentManager1.getBackStackEntryAt(index);
                                String tag1 = backStackEntry1.getName();
                                if (tag1 == "History" || tag1 == "Profile" || tag1 == "Settings") {
                                    fragmentManager1.popBackStack();
                                    fragmentManager1.beginTransaction()
                                            .replace(R.id.frame, history1, history1.getTag())
                                            .addToBackStack("History")
                                            .commit();
                                } else {
                                    fragmentManager1.beginTransaction()
                                            .replace(R.id.frame, history1, history1.getTag())
                                            .addToBackStack("History")
                                            .commit();
                                }
                            }
                            break;
                        case R.id.action_profile:
                            item.setChecked(item.getItemId() == 2);
                            Profile profile1 = new Profile();
                            FragmentManager fragmentManager2 = getSupportFragmentManager();
                            index = fragmentManager2.getBackStackEntryCount();
                            if (index == 0) {
                                fragmentManager2.beginTransaction()
                                        .replace(R.id.frame, profile1, profile1.getTag())
                                        .addToBackStack("Profile")
                                        .commit();
                            } else {
                                index = index - 1;
                                FragmentManager.BackStackEntry backStackEntry2 = fragmentManager2.getBackStackEntryAt(index);
                                String tag2 = backStackEntry2.getName();
                                if (tag2 == "History" || tag2 == "Profile" || tag2 == "Settings") {
                                    fragmentManager2.popBackStack();
                                    fragmentManager2.beginTransaction()
                                            .replace(R.id.frame, profile1, profile1.getTag())
                                            .addToBackStack("Profile")
                                            .commit();
                                } else {
                                    fragmentManager2.beginTransaction()
                                            .replace(R.id.frame, profile1, profile1.getTag())
                                            .addToBackStack("Profile")
                                            .commit();
                                }
                            }
                            break;
                        case R.id.action_settings:
                            item.setChecked(item.getItemId() == 3);
                            Settings settings1 = new Settings();
                            FragmentManager fragmentManager3 = getSupportFragmentManager();
                            index = fragmentManager3.getBackStackEntryCount();
                            if (index == 0) {
                                fragmentManager3.beginTransaction()
                                        .replace(R.id.frame, settings1, settings1.getTag())
                                        .addToBackStack("Settings")
                                        .commit();
                            } else {
                                index = index - 1;
                                FragmentManager.BackStackEntry backStackEntry3 = fragmentManager3.getBackStackEntryAt(index);
                                String tag3 = backStackEntry3.getName();
                                if (tag3 == "History" || tag3 == "Profile" || tag3 == "Settings") {
                                    fragmentManager3.popBackStack();
                                    fragmentManager3.beginTransaction()
                                            .replace(R.id.frame, settings1, settings1.getTag())
                                            .addToBackStack("Settings")
                                            .commit();
                                } else {
                                    fragmentManager3.beginTransaction()
                                            .replace(R.id.frame, settings1, settings1.getTag())
                                            .addToBackStack("Settings")
                                            .commit();
                                }
                            }
                            break;
                    }
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_parking);


    }


    public void onBackPressed() {
        index = getSupportFragmentManager().getBackStackEntryCount();
        if (index == 0) {
            super.onBackPressed();
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_parking);
        }
    }
    public void start() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, true);
        editor.apply();
    }
}
