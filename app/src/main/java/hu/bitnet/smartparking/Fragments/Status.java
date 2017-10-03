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
    TextView amount_pay;
    long count = 0;
    String sessionId, id;
    Timer T;
    double timeHour;
    double timeMin;
    double timeSec;
    String timeHourString;
    String timeMinString;
    String timeSecString;
    String price;
    double priceDouble;
    double priceDouble2;

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
        amount_pay = (TextView)status.findViewById(R.id.amount_pay);
        pref = getActivity().getPreferences(0);
        sessionId = pref.getString("sessionId", null);
        id = pref.getString("id", null);

        loadJSON(sessionId, id);


        AppCompatButton stopparking = (AppCompatButton) status.findViewById(R.id.stop_parking);
        stopparking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJSONStop(sessionId, id);
                T.cancel();
                T = null;
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
                    price = resp.getPlace().getPrice().toString();
                    priceDouble = Double.parseDouble(price);
                    if(T == null){
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
                                        timeHour = Math.floor(count/3600);
                                        timeMin = Math.floor((count-timeHour*3600)/60);
                                        timeSec = count-timeHour*3600-timeMin*60;
                                        if(timeHour < 10){
                                            timeHourString = "0"+Integer.toString((int)timeHour);
                                        }else{
                                            timeHourString = Integer.toString((int)timeHour);
                                        }
                                        if(timeMin < 10){
                                            timeMinString = "0"+Integer.toString((int)timeMin);
                                        }else{
                                            timeMinString = Integer.toString((int)timeMin);
                                        }
                                        if(timeSec < 10){
                                            timeSecString = "0"+Integer.toString((int)timeSec);
                                        }else{
                                            timeSecString = Integer.toString((int)timeSec);
                                        }
                                        priceDouble2 = Math.ceil(priceDouble * Double.valueOf(Long.toString(count))/3600.0);
                                        status_text.setText(timeHourString + ":" + timeMinString + ":" + timeSecString);
                                        amount_pay.setText(Integer.toString((int)priceDouble2) + " Ft");
                                        count++;
                                    }
                                });
                            }
                        }, 1000, 1000);
                    }
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
