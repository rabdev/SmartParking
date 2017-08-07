package hu.bitnet.smartparking.Fragments;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.R;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceRegister;
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
public class Registration extends Fragment {

    public AppCompatButton btn_register;
    public EditText reg_email;
    public EditText reg_password;
    public EditText reg_first_name;
    public EditText reg_last_name;
    public EditText reg_phone;

    public Registration() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View registration = inflater.inflate(R.layout.fragment_registration, container, false);
        TextView tv_login = (TextView) registration.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainframe, login, login.getTag())
                        .commit();
            }
        });


        btn_register = (AppCompatButton)registration.findViewById(R.id.btn_register);
        reg_email = (EditText)registration.findViewById(R.id.reg_email);
        reg_password = (EditText)registration.findViewById(R.id.reg_password);
        reg_first_name = (EditText)registration.findViewById(R.id.reg_first_name);
        reg_last_name = (EditText)registration.findViewById(R.id.reg_last_name);
        reg_phone = (EditText)registration.findViewById(R.id.reg_phone);
        RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.appbar);
        relativeLayout.setVisibility(View.GONE);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navbar);
        bottomNavigationView.setVisibility(View.GONE);

        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = reg_email.getText().toString();
                String password = reg_password.getText().toString();
                String first_name = reg_first_name.getText().toString();
                String last_name = reg_last_name.getText().toString();
                String phone = reg_phone.getText().toString();
                loadJSON(email, password, first_name, last_name, phone);
            }
        });

        return registration;
    }

    public void loadJSON(String email, String password, String first_name, String last_name, String phone){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.SERVER_URL)
                //.baseUrl("http://jasehn.eu/homokozo/SmartPark/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterfaceRegister requestInterface = retrofit.create(RequestInterfaceRegister.class);
        Call<ServerResponse> response= requestInterface.post(email, first_name, last_name, password, phone);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Toast.makeText(getContext(), resp.getError().getMessage()+" - "+resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(TAG, "No response");
            }
        });

    }

}
