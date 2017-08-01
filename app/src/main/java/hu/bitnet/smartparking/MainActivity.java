package hu.bitnet.smartparking;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenu;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import hu.bitnet.smartparking.Fragments.Home;
import hu.bitnet.smartparking.Fragments.Profile;
import hu.bitnet.smartparking.Fragments.Settings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View bottomNavigationMenu = findViewById(R.id.bottom_navbar);
        BottomNavigationItemView parking = (BottomNavigationItemView) findViewById(R.id.action_parking);
        BottomNavigationItemView history = (BottomNavigationItemView) findViewById(R.id.action_history);
        BottomNavigationItemView profile = (BottomNavigationItemView) findViewById(R.id.action_profile);
        BottomNavigationItemView settings = (BottomNavigationItemView) findViewById(R.id.action_settings);

        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home home = new Home();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, home, home.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile1 = new Profile();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, profile1, profile1.getTag())
                        .commit();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings settings1 = new Settings();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, settings1, settings1.getTag())
                        .commit();
            }
        });


    }
}
