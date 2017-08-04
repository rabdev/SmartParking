package hu.bitnet.smartparking.Fragments;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import hu.bitnet.smartparking.Constants;
import hu.bitnet.smartparking.R;
import hu.bitnet.smartparking.RequestInterface;
import hu.bitnet.smartparking.ServerRequest;
import hu.bitnet.smartparking.ServerResponse;
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
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setEmail(email);
        request.setPassword(password);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Toast.makeText(getContext(), "valamit azért kiírok", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), resp.getError().getMessage()+" - "+resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "valamit azért kiírok itt", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No response");
            }
        });

    }

}
