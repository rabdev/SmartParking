package hu.bitnet.smartparking.Fragments;


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Parking extends Fragment {

    SharedPreferences pref;

    public Parking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parking =inflater.inflate(R.layout.fragment_parking, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Find a parking");
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.ic_back);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setVisibility(View.GONE);
        TextView address = (TextView)parking.findViewById(R.id.address);
        TextView price = (TextView)parking.findViewById(R.id.price);
        Switch switch1 = (Switch)parking.findViewById(R.id.switch1);

        pref = getActivity().getPreferences(0);
        final String sessionId = pref.getString("sessionId", null);
        final String id = pref.getString("id", null);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    loadJSONStart(sessionId, id);
                }
                if(isChecked == false){
                    loadJSONStop(sessionId, id);
                }
            }
        });

        address.setText(pref.getString("address", null));
        price.setText(pref.getString("price", null));

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
                    FragmentTransaction status = getFragmentManager().beginTransaction();
                    status.replace(R.id.frame, new Status()).addToBackStack(null);
                    status.commit();
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
}
