package hu.bitnet.smartparking.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hu.bitnet.smartparking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View home = inflater.inflate(R.layout.fragment_home, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Find a parking");

        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView.setImageResource(R.drawable.ic_account);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView1.setImageResource(R.drawable.ic_history);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainframe, login, login.getTag())
                        .commit();
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration registration = new Registration();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainframe, registration, registration.getTag())
                        .commit();
            }
        });

        AppCompatButton map = (AppCompatButton) home.findViewById(R.id.btn_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map1 = new Map();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, map1, map1.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return home;
    }

}
