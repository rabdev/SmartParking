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

import hu.bitnet.smartparking.Fragments.Home;
import hu.bitnet.smartparking.Fragments.Login;
import hu.bitnet.smartparking.Fragments.Profile;
import hu.bitnet.smartparking.Fragments.Registration;
import hu.bitnet.smartparking.Fragments.Settings;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getPreferences(0);

        /*if (preferences.getBoolean(Constants.IS_LOGGED_IN,true)){
            setContentView(R.layout.activity_main);
        } else {

            Login login = new Login();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainframe, login, login.getTag())
                    .commit();
        }*/


        final BottomNavigationItemView parking = (BottomNavigationItemView) findViewById(R.id.action_parking);
        final BottomNavigationItemView history = (BottomNavigationItemView) findViewById(R.id.action_history);
        final BottomNavigationItemView profile = (BottomNavigationItemView) findViewById(R.id.action_profile);
        final BottomNavigationItemView settings = (BottomNavigationItemView) findViewById(R.id.action_settings);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navbar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.appbar);
        relativeLayout.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()== R.id.action_parking){
                    item.setChecked(item.getItemId()== 0);
                    Home home = new Home();
                    Registration registration = new Registration();
                    Login login = new Login();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            //.replace(R.id.frame, home, home.getTag())
                            //.replace(R.id.frame, registration, registration.getTag())
                            .replace(R.id.frame, login, login.getTag())
                            .addToBackStack(null)
                            .commit();
                } else {
                    switch (item.getItemId()) {
                        /*case R.id.action_history:

                            break;*/
                        case R.id.action_profile:
                            item.setChecked(item.getItemId() == 2);
                            Profile profile1 = new Profile();
                            FragmentManager fragmentManager1 = getSupportFragmentManager();
                            fragmentManager1.beginTransaction()
                                    .replace(R.id.frame, profile1, profile1.getTag())
                                    .commit();
                            break;
                        case R.id.action_settings:
                            item.setChecked(item.getItemId() == 3);
                            Settings settings1 = new Settings();
                            FragmentManager fragmentManager2 = getSupportFragmentManager();
                            fragmentManager2.beginTransaction()
                                    .replace(R.id.frame, settings1, settings1.getTag())
                                    .commit();
                            break;
                    }
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_parking);




    }
}
