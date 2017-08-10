package hu.bitnet.smartparking;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import hu.bitnet.smartparking.Fragments.History;
import hu.bitnet.smartparking.Fragments.Home;
import hu.bitnet.smartparking.Fragments.Login;
import hu.bitnet.smartparking.Fragments.Profile;
import hu.bitnet.smartparking.Fragments.Settings;
import hu.bitnet.smartparking.Fragments.Status;
import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceParkingStatus;
import hu.bitnet.smartparking.ServerResponses.ServerResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    BottomNavigationView bottomNavigationView;
    Context context;
    int index;
    Boolean statusBool;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getPreferences(0);
        //start();
        String sessionId = preferences.getString("sessionId", null);
        String id = preferences.getString("id", null);

        if (preferences.getBoolean(Constants.IS_LOGGED_IN, true)) {
            setContentView(R.layout.activity_main);
            loadJSON(sessionId, id);
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
        Fragment status = getSupportFragmentManager().findFragmentByTag("Status");
        Fragment finish = getSupportFragmentManager().findFragmentByTag("Finish");
        if (status==null & finish==null){
            if (index == 0) {
                super.onBackPressed();
            } else {
                bottomNavigationView.setSelectedItemId(R.id.action_parking);
            }
        }

    }
    public void start() {
        SharedPreferences.Editor editor = preferences.edit();
        //editor.putString("sessionID","1");
        editor.remove("latitude");
        editor.remove("longitude");
        editor.apply();
    }

    public void loadJSON(String sessionId, String id) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterfaceParkingStatus requestInterface = retrofit.create(RequestInterfaceParkingStatus.class);
        Call<ServerResponse> response = requestInterface.post(sessionId, id);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();

                if (resp.getError() != null) {
                    message = resp.getError().getMessage() + " - " + resp.getError().getMessageDetail();
                }
                if (resp.getSum() != null){
                    Toast.makeText(getApplicationContext(), "Parkolása folyamatban!", Toast.LENGTH_LONG).show();
                    Status status = new Status();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, status, status.getTag())
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Hiba a hálózati kapcsolatban. Kérjük, ellenőrizze, hogy csatlakozik-e hálózathoz.", Toast.LENGTH_SHORT).show();
                Log.d(ContentValues.TAG, "No response");
            }
        });
    }

}