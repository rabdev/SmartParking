package hu.bitnet.smartparking.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import hu.bitnet.smartparking.Adapters.SearchAdapter;
import hu.bitnet.smartparking.MainActivity;
import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.Objects.Parking_places;
import hu.bitnet.smartparking.R;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceNearest;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceParkingSelect;
import hu.bitnet.smartparking.ServerResponses.ServerResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByLoc extends Fragment {

    private RecyclerView mRecyclerView;
    SharedPreferences pref;
    public String sessionId;
    public ArrayList<Parking_places> data;
    public SearchAdapter mAdapter;
    //public String longitude;
    //public String latitude;
    private Context context;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    private double latitude, longitude;

    LocationManager locationManager;

    public SearchByLoc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View search = inflater.inflate(R.layout.fragment_search, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Select parking spot");
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.ic_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) search.findViewById(R.id.contacts_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        pref = getActivity().getPreferences(0);
        sessionId = pref.getString("sessionId", null);

        Location location = getLocation();

        if(location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
        }else{
            Toast.makeText(getContext(), "Engedélyezze eszközén a helymeghatározást!", Toast.LENGTH_LONG).show();
        }

        loadJSON(sessionId, Double.toString(latitude), Double.toString(longitude));
        return search;
    }

    public void loadJSON(String sessionId, String latitude, String longitude){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterfaceNearest requestInterface = retrofit.create(RequestInterfaceNearest.class);
        Call<ServerResponse> response= requestInterface.post(sessionId, latitude, longitude);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if(resp.getAlert() != ""){
                    Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_LONG).show();
                }
                if(resp.getError() != null){
                    Toast.makeText(getContext(), resp.getError().getMessage()+" - "+resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN,false);
                    editor.apply();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(resp.getParking_places() != null){
                    data = new ArrayList<Parking_places>(Arrays.asList(resp.getParking_places()));
                    mAdapter = new SearchAdapter(data);
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new SearchAdapter.ClickListener(){
                        @Override
                        public void onItemClick(final int position, View v){
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("address", data.get(position).getAddress().toString());
                            editor.putString("price", data.get(position).getPrice().toString());
                            editor.putString("id", data.get(position).getId().toString());
                            editor.putString("latitude", data.get(position).getLatitude().toString());
                            editor.putString("longitude", data.get(position).getLongitude().toString());
                            editor.putString("distance", data.get(position).getDistance().toString());
                            editor.putString("time", data.get(position).getTime().toString());
                            editor.apply();
                            String id = data.get(position).getId().toString();
                            String sessionId = pref.getString("sessionId", null);
                            loadJSONSelect(sessionId, id);
                            FragmentManager map = getActivity().getSupportFragmentManager();
                            map.beginTransaction()
                                    .replace(R.id.frame, new Map())
                                    .addToBackStack(null)
                                    .commit();
                        }

                        @Override
                        public void onItemLongClick(int position, View v) {
                            Log.d(TAG, "onItemLongClick pos = " + position);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hiba a hálózati kapcsolatban. Kérjük, ellenőrizze, hogy csatlakozik-e hálózathoz.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No response");
            }
        });

    }

    public void loadJSONSelect(String sessionId, String id){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterfaceParkingSelect requestInterface = retrofit.create(RequestInterfaceParkingSelect.class);
        Call<ServerResponse> response= requestInterface.post(sessionId, id);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if(resp.getAlert() != ""){
                    Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_LONG).show();
                }
                if(resp.getError() != null){
                    Toast.makeText(getContext(), resp.getError().getMessage()+" - "+resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
                }
                if(resp.getMQTT() != null){
                    //Toast.makeText(getContext(), resp.getMQTT().getHost().toString(), Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("host", resp.getMQTT().getHost());
                    editor.putString("port", resp.getMQTT().getPort());
                    editor.putString("topic", resp.getMQTT().getTopic());
                    editor.apply();
                }
                if(resp.getBLE() != null){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("service", resp.getBLE().getService());
                    editor.putString("characteristic", resp.getBLE().getCharacteristic());
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hiba a hálózati kapcsolatban. Kérjük, ellenőrizze, hogy csatlakozik-e hálózathoz.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No response");
            }
        });

    }

    private Location getLocation() {
        // TODO Auto-generated method stub
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Log.d(TAG, "nagy nulla");
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 3, locationListener);

                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled){
                    if (location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 3, locationListener);
                        if (locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                } else {
                    //showAlertDialog();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
