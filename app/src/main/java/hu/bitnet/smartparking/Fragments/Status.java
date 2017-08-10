package hu.bitnet.smartparking.Fragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
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
import java.util.TimerTask;

import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.R;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceParkingStatus;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceParkingStop;
import hu.bitnet.smartparking.ServerResponses.ServerResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.gms.internal.zzagz.runOnUiThread;
import static com.google.android.gms.wearable.DataMap.TAG;
import static java.lang.Long.parseLong;


public class Status extends Fragment {

    SharedPreferences pref;
    TextView status_text;
    long count = 0;
    String sessionId, id;
    Timer T;

    public Status() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View status =inflater.inflate(R.layout.fragment_status, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Parking under process");
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.GONE);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setVisibility(View.GONE);

        status_text = (TextView)status.findViewById(R.id.status);
        pref = getActivity().getPreferences(0);
        sessionId = pref.getString("sessionId", null);
        id = pref.getString("id", null);

        loadJSON(sessionId, id);


        AppCompatButton stopparking = (AppCompatButton) status.findViewById(R.id.stop_parking);
        stopparking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJSONStop(sessionId, id);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("latitude");
                editor.remove("longitude");
                editor.apply();
                Log.d(TAG, "time2: "+pref.getString("ParkTime", null));
                Finish finish = new Finish();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, finish, "Finish")
                        .addToBackStack("Finish")
                        .commit();

            }
        });

        /*switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    loadJSONStop(sessionId, id);
                }
            }
        });*/

        return status;
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
                    //Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_LONG).show();
                }
                if (resp.getError() != null) {
                    Toast.makeText(getContext(), resp.getError().getMessage() + " - " + resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
                }
                if (resp.getStart() != null) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("ParkTime", resp.getTime().toString());
                    Log.d(TAG, "time: "+resp.getTime().toString());
                    editor.putString("ParkPrice", resp.getPrice().toString());
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hiba a hálózati kapcsolatban. Kérjük, ellenőrizze, hogy csatlakozik-e hálózathoz.", Toast.LENGTH_SHORT).show();
                Log.d(ContentValues.TAG, "No response");
            }
        });
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
                if (resp.getAlert() != "") {
                    Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_LONG).show();
                }
                if (resp.getError() != null) {
                    Toast.makeText(getContext(), resp.getError().getMessage() + " - " + resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
                }
                if (resp.getSum() != null) {
                    T=new Timer();
                    count = System.currentTimeMillis()/1000-parseLong(resp.getSum().getStart());
                    T.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    status_text.setText(""+count);
                                    count++;
                                }
                            });
                        }
                    }, 1000, 1000);
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
