package hu.bitnet.smartparking.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hu.bitnet.smartparking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {


    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View history = inflater.inflate(R.layout.fragment_history, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("History");
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.GONE);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setVisibility(View.GONE);
        return history;
    }

}