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
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceLogin;
import hu.bitnet.smartparking.ServerRequests.ServerRequestLogin;
import hu.bitnet.smartparking.ServerResponses.ServerResponseLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    public AppCompatButton btn_login;
    public EditText et_email;
    public EditText et_password;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View login = inflater.inflate(R.layout.fragment_login, container, false);
        AppCompatButton btn_login = (AppCompatButton)login.findViewById(R.id.btn_login);
        et_email = (EditText)login.findViewById(R.id.et_email);
        et_password = (EditText)login.findViewById(R.id.et_password);
        RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.appbar);
        relativeLayout.setVisibility(View.GONE);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navbar);
        bottomNavigationView.setVisibility(View.GONE);

        TextView tv_register = (TextView) login.findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration registration = new Registration();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainframe, registration, registration.getTag())
                        .commit();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                loadJSON(email, password);
            }
        });

        return login;
    }

    public void loadJSON(String email, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterfaceLogin requestInterface = retrofit.create(RequestInterfaceLogin.class);
        ServerRequestLogin request = new ServerRequestLogin();
        request.setEmail("b@a.hu");
        request.setPassword("1234");
        Call<ServerResponseLogin> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponseLogin>() {
            @Override
            public void onResponse(Call<ServerResponseLogin> call, retrofit2.Response<ServerResponseLogin> response) {
                ServerResponseLogin resp = response.body();
                Toast.makeText(getContext(), "valamit azért kiírok", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), resp.getError().getMessage()+" - "+resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponseLogin> call, Throwable t) {
                Toast.makeText(getContext(), "valamit azért kiírok itt", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No response");
            }
        });

    }

}
