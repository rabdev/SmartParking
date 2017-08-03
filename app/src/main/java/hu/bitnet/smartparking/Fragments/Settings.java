package hu.bitnet.smartparking.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.bitnet.smartparking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View settings =inflater.inflate(R.layout.fragment_settings, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Settings");

        return settings;
    }

}
