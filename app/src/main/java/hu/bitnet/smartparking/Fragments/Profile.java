package hu.bitnet.smartparking.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    SharedPreferences pref;
    TextView first_name, last_name, email, phone;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profile = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Profile");
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.GONE);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setVisibility(View.GONE);
        AppCompatButton btn_logout = (AppCompatButton) profile.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref=getActivity().getPreferences(0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(Constants.IS_LOGGED_IN,false);
                editor.putBoolean("OngoingParkingStatus", false);
                editor.apply();
                Intent intent = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                startActivity(intent);

            }
        });

        first_name= (TextView) profile.findViewById(R.id.profile_first_name);
        last_name = (TextView) profile.findViewById(R.id.profile_last_name);
        email = (TextView) profile.findViewById(R.id.profile_email);
        phone = (TextView) profile.findViewById(R.id.profile_phone);

        SharedPreferences pref;
        pref = getActivity().getPreferences(0);
        String firstName = pref.getString("firstName", null);
        String lastName = pref.getString("lastName", null);
        String email_string = pref.getString("email", null);
        String phone_string = pref.getString("phone", null);

        first_name.setText(firstName);
        last_name.setText(lastName);
        email.setText(email_string);
        phone.setText(phone_string);

        return profile;
    }

}
