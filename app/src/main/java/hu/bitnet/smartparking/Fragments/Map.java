package hu.bitnet.smartparking.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;

import hu.bitnet.smartparking.GMapV2Direction;
import hu.bitnet.smartparking.GMapV2DirectionAsyncTask;
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
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 */
public class Map extends Fragment implements LocationListener, OnMapReadyCallback, LocationSource.OnLocationChangedListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    public MapView mapView;
    public GoogleMap gmap;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    android.location.LocationListener locationlistener;
    LatLng latlng;
    Location location;
    Polyline polylin;
    double latitude, x;
    double longitude, y;
    public final static int MILLISECONDS_PER_SECOND = 1000;
    public final static int MINUTE = 60 * MILLISECONDS_PER_SECOND;
    SharedPreferences pref;
    public Marker marker;
    LinearLayout mapcard, navigate;
    TextView mapaddress, mapperprice, mapdistance, maptraffic;
    ArrayList<Parking_places> data;


    public Map() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View map = inflater.inflate(R.layout.fragment_map, container, false);
        pref = getActivity().getPreferences(0);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Select parking spot");

        mapcard= (LinearLayout) map.findViewById(R.id.map_card);
        navigate = (LinearLayout) map.findViewById(R.id.navigate);
        mapaddress = (TextView) map.findViewById(R.id.map_address);
        mapperprice = (TextView) map.findViewById(R.id.map_perprice);
        mapdistance = (TextView) map.findViewById(R.id.map_distance);
        maptraffic = (TextView) map.findViewById(R.id.map_traffic);

        navigate.setVisibility(View.GONE);
        mapcard.setVisibility(View.GONE);


        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView.setImageResource(R.drawable.ic_navigate);
        imageView.setVisibility(View.VISIBLE);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView1.setImageResource(R.drawable.ic_back);
        imageView1.setVisibility(View.VISIBLE);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (polylin != null) {
                    polylin.remove();
                }
                if (marker == null) {
                    Snackbar.make(map, "Nem választottál célt!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if (location != null) {
                        double c = location.getLatitude();
                        double d = location.getLongitude();
                        LatLng sourcePosition = new LatLng(c, d);
                        LatLng destPosition = marker.getPosition();
                        final Handler handler = new Handler() {
                            public void handleMessage(Message msg) {
                                try {
                                    Document doc = (Document) msg.obj;
                                    GMapV2Direction md = new GMapV2Direction();
                                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                                    PolylineOptions rectLine = new PolylineOptions().width(12).color(Color.rgb(15,192,114));

                                    for (int i = 0; i < directionPoint.size(); i++) {
                                        rectLine.add(directionPoint.get(i));
                                    }
                                    polylin = gmap.addPolyline(rectLine);
                                    md.getDurationText(doc);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, GMapV2Direction.MODE_DRIVING).execute();
                        navigate.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mapView = (MapView) map.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
        mGoogleApiClient.connect();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MINUTE);
        mLocationRequest.setFastestInterval(15 * MILLISECONDS_PER_SECOND);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        String lat = pref.getString("latitude", null);
        String lon = pref.getString("longitude", null);

        if (lat!= null & lon !=null){
            mapaddress.setText(pref.getString("address", null));
            mapperprice.setText(pref.getString("price", null) + " Ft/óra");
            mapdistance.setText(pref.getString("distance",null));
            maptraffic.setText(pref.getString("time",null)+" mins without traffic");
            mapcard.setVisibility(View.VISIBLE);
        }
        mapcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJSONSelect(pref.getString("sessionId", null), pref.getString("id", null));
                FragmentManager parking = getActivity().getSupportFragmentManager();
                parking.beginTransaction()
                        .replace(R.id.frame, new Parking())
                        .addToBackStack(null)
                        .commit();
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format("http://maps.google.com/maps?" + "saddr="+latitude+","+longitude+ "&daddr="+x+","+y);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });

        return map;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location loc) {
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();
        location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMyLocationEnabled(true);
        gmap.getUiSettings().setMyLocationButtonEnabled(true);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        locationlistener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                double latitude = loc.getLatitude();
                double longitude = loc.getLongitude();
                location = new Location("");
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                return;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String bestProvider) {
                LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET
                        }, 10);
                        return;
                    }
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationlistener);
                } else {
                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistener);
                }
            }

            @Override
            public void onProviderDisabled(String s) {
                LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET
                        }, 10);
                        return;
                    }
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
                } else {
                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistener);
                }
            }
        };
        location = locationManager.getLastKnownLocation(bestProvider);
        if (location == null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
            locationManager.requestLocationUpdates(bestProvider, 0, 0, locationlistener);
            return;
        } else {
            double c = location.getLatitude();
            double d = location.getLongitude();
            LatLng myloc = new LatLng(c, d);
            gmap.animateCamera(CameraUpdateFactory.newLatLng(myloc));
            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc, 12));
        }

        final String lat = pref.getString("latitude", null);
        final String lon = pref.getString("longitude", null);

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.d(TAG, "Lat: "+latitude);
        Log.d(TAG, "long: "+longitude);

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
        Call<ServerResponse> response= requestInterface.post(pref.getString("sessionId", null), Double.toString(latitude), Double.toString(longitude));
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
                    Log.d(TAG, "data: "+data.get(0).getLatitude());
                    if (lat !=null & lon !=null) {
                        x = Double.parseDouble(lat);
                        y = Double.parseDouble(lon);
                        LatLng latLng = new LatLng(x, y);
                        marker = gmap.addMarker(new MarkerOptions().position(latLng));
                        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    } else {
                        ArrayList<Parking_places> markersArray = new ArrayList<>();
                        Log.d(TAG, "size: "+data.size());
                        for (int i = 0; i < data.size(); i++) {
                            markersArray.add(new Parking_places(data.get(i).getLatitude(), data.get(i).getLongitude(), data.get(i).getAddress()));
                        }

                        for(int i = 0 ; i < markersArray.size() ; i++ ) {

                            createMarker(markersArray.get(i).getLatitude(), markersArray.get(i).getLongitude(), markersArray.get(i).getAddress());
                        }

                    }
                    gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker1) {
                            /*Toast.makeText(getContext(), String.valueOf(marker1.getPosition())
                                    + String.valueOf(marker1.getTitle()), Toast.LENGTH_LONG).show();*/

                            mapcard.setVisibility(View.VISIBLE);
                            marker1.hideInfoWindow();
                            LatLng position = marker1.getPosition();
                            x = position.latitude;
                            y = position.longitude;
                            marker=marker1;
                            mapaddress.setText(marker1.getTitle());
                            mapperprice.setText(String.format("%.0f", Double.parseDouble(data.get(parseInt(marker1.getId().substring(1))).getPrice())) + " Ft/óra");
                            mapdistance.setText(String.format("%.1f", Double.parseDouble(data.get(parseInt(marker1.getId().substring(1))).getDistance())) + " km");
                            maptraffic.setText(String.format("%.1f", Double.parseDouble(data.get(parseInt(marker1.getId().substring(1))).getTime())/60.0) + " mins");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("id", data.get(parseInt(marker1.getId().substring(1))).getId());
                            editor.putString("address", data.get(parseInt(marker1.getId().substring(1))).getAddress());
                            editor.putString("price", String.format("%.0f", Double.parseDouble(data.get(parseInt(marker1.getId().substring(1))).getPrice())));
                            /*editor.putString("host", data.get(parseInt(marker1.getId().substring(1))).getMQTT().getHost());
                            editor.putString("port", data.get(parseInt(marker1.getId().substring(1))).getMQTT().getPort());
                            editor.putString("topic", data.get(parseInt(marker1.getId().substring(1))).getMQTT().getTopic());
                            editor.putString("service", data.get(parseInt(marker1.getId().substring(1))).getBLE().getService());
                            editor.putString("characteristic", data.get(parseInt(marker1.getId().substring(1))).getBLE().getCharacteristic());*/
                            editor.apply();
                            return false;
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


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    protected Marker createMarker(String parklat, String parklong, String address) {

        //, String title, String snippet, int iconResID
        double parklatitude = Double.parseDouble(parklat);
        double parklongitude = Double.parseDouble(parklong);

        return gmap.addMarker(new MarkerOptions()
                .position(new LatLng(parklatitude, parklongitude))
                .title(address));
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
}
