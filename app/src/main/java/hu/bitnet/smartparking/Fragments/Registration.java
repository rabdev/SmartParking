package hu.bitnet.smartparking.Fragments;


import android.os.AsyncTask;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.bitnet.smartparking.Constants;
import hu.bitnet.smartparking.R;
import hu.bitnet.smartparking.RequestInterfaces.RequestInterfaceRegister;
import hu.bitnet.smartparking.ServerResponse;
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
                java.util.Map<String,String> myMap1 = new HashMap<String, String>();
                myMap1.put("email", email);
                myMap1.put("password", password);
                myMap1.put("firstName", first_name);
                myMap1.put("lastName", last_name);
                myMap1.put("phone", phone);
                loadJSON(email, password, first_name, last_name, phone);
                //new DownloadTask().execute("myMap");
            }
        });

        return registration;
    }

    public void loadJSON(String email, String password, String first_name, String last_name, String phone){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                //.baseUrl("http://jasehn.eu/homokozo/SmartPark/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterfaceRegister requestInterface = retrofit.create(RequestInterfaceRegister.class);
        /*ServerRequest request = new ServerRequest();
        Profile profile = new Profile();
        profile.setEmail(email);
        profile.setPassword(password);
        profile.setFirstName(first_name);
        profile.setLastName(last_name);
        profile.setPhone(phone);
        request.setProfile(profile);
        Log.d(TAG, "EMAIL: "+email);
        request.setPassword(password);
        request.setFirstName(first_name);
        request.setLastName(last_name);
        request.setPhone(phone);
        Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();*/
        Call<ServerResponse> response = requestInterface.post(first_name, last_name, email, password, phone);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Toast.makeText(getContext(), resp.getError().getMessage()+" - "+resp.getError().getMessageDetail(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), resp.getProfile().getSessionid(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), resp.getAlert(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(TAG, "No response");
            }
        });

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //do your request in here so that you don't interrupt the UI thread
            try {
                return downloadContent(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 500;

        try {
            URL url = new URL(Constants.SERVER_URL+"register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", "a@b.hu"));
            params.add(new BasicNameValuePair("firstName", "A"));
            params.add(new BasicNameValuePair("lastName", "B"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertInputStreamToString(is, length);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
