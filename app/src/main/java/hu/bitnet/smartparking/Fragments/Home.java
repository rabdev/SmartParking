package hu.bitnet.smartparking.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
public class Home extends Fragment {

    SharedPreferences pref;

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
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.GONE);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setImageResource(R.drawable.ic_exit);
        imageView1.setVisibility(View.VISIBLE);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = getActivity().getPreferences(0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(Constants.IS_LOGGED_IN,false);
                editor.apply();
                Intent intent = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                startActivity(intent);
            }
        });

        AppCompatButton map = (AppCompatButton) home.findViewById(R.id.btn_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map1 = new Map();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, map1, "Map")
                        .addToBackStack(null)
                        .commit();
            }
        });

        AppCompatButton search = (AppCompatButton) home.findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search search1 = new Search();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, search1, "Search")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return home;
    }

}
