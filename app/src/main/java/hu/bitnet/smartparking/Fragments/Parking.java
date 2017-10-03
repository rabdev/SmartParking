package hu.bitnet.smartparking.Fragments;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.R;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceParkingSelect;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceParkingStart;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceParkingStop;
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
public class Parking extends Fragment {

    SharedPreferences pref;
    AppCompatButton startparking;
    String sessionId, id;
    View parking, stop;
    Timer T;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    LocationManager locationManager;

    public Parking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parking =inflater.inflate(R.layout.fragment_parking, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Start parking");
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.ic_back);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setVisibility(View.GONE);
        TextView address = (TextView)parking.findViewById(R.id.address);
        TextView price = (TextView)parking.findViewById(R.id.price);

        pref = getActivity().getPreferences(0);
        sessionId = pref.getString("sessionId", null);
        id = pref.getString("id", null);

        startparking= (AppCompatButton) parking.findViewById(R.id.start_parking);
        startparking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJSONStart(sessionId, id);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(String.valueOf(Constants.PAYED),false);
                editor.apply();

            }
        });

        address.setText(pref.getString("address", null));
        price.setText(pref.getString("price", null) + " Ft/óra");

        return parking;
    }

    public void loadJSONStart(String sessionId, String id) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterfaceParkingStart requestInterface = retrofit.create(RequestInterfaceParkingStart.class);
        Call<ServerResponse> response = requestInterface.post(sessionId, id);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getAlert() != "") {
                    Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_LONG).show();
                    Status status = new Status();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, status, "Status")
                            .addToBackStack("Status")
                            .commit();
                }
                if (resp.getError() != null) {
                    Toast.makeText(getContext(), resp.getError().getMessage() + " - " + resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hiba a hálózati kapcsolatban. Kérjük, ellenőrizze, hogy csatlakozik-e hálózathoz.", Toast.LENGTH_SHORT).show();
                Log.d(ContentValues.TAG, "No response");
            }
        });
    }

    public void loadJSONStop(String sessionId, String id) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterfaceParkingStop requestInterface = retrofit.create(RequestInterfaceParkingStop.class);
        Call<ServerResponse> response = requestInterface.post(sessionId, id);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getAlert() != "") {
                    Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_LONG).show();
                }
                if (resp.getError() != null) {
                    Toast.makeText(getContext(), resp.getError().getMessage() + " - " + resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
                }
                if (resp.getAddress() != null) {

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hiba a hálózati kapcsolatban. Kérjük, ellenőrizze, hogy csatlakozik-e hálózathoz.", Toast.LENGTH_SHORT).show();
                Log.d(ContentValues.TAG, "No response");
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

}
