package hu.bitnet.smartparking;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;

import hu.bitnet.smartparking.Fragments.Home;
import hu.bitnet.smartparking.Fragments.Map;
import hu.bitnet.smartparking.Fragments.Profile;
import hu.bitnet.smartparking.Fragments.Settings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationItemView parking = (BottomNavigationItemView) findViewById(R.id.action_parking);
        final BottomNavigationItemView history = (BottomNavigationItemView) findViewById(R.id.action_history);
        final BottomNavigationItemView profile = (BottomNavigationItemView) findViewById(R.id.action_profile);
        final BottomNavigationItemView settings = (BottomNavigationItemView) findViewById(R.id.action_settings);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_parking:
                        item.setChecked(item.getItemId()== 0);
                        Home home = new Home();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame, home, home.getTag())
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.action_profile:
                        item.setChecked(item.getItemId()== 2);
                        Profile profile1 = new Profile();
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        fragmentManager1.beginTransaction()
                                .replace(R.id.frame, profile1, profile1.getTag())
                                .commit();
                        break;
                    case R.id.action_settings:
                        item.setChecked(item.getItemId()== 3);
                        Settings settings1 = new Settings();
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        fragmentManager2.beginTransaction()
                                .replace(R.id.frame, settings1, settings1.getTag())
                                .commit();
                        break;
                }

                return false;
            }
        });





    }
}
