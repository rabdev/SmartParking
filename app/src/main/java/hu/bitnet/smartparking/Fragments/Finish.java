package hu.bitnet.smartparking.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.R;

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 */
public class Finish extends Fragment {

    SharedPreferences pref;
    Integer timeInt;
    double timeHour;
    double timeMin;
    double timeSec;
    String timeHourString;
    String timeMinString;
    String timeSecString;

    public Finish() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View finish = inflater.inflate(R.layout.fragment_finish, container, false);
        TextView appbartext = (TextView) getActivity().findViewById(R.id.appbar_text);
        appbartext.setText("Parking finished");
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.VISIBLE);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        TextView time = (TextView)finish.findViewById(R.id.status);
        TextView price = (TextView)finish.findViewById(R.id.amount_pay);

        pref = getActivity().getPreferences(0);
        timeInt = parseInt(pref.getString("ParkTime", null));
        timeHour = Math.floor(timeInt/3600);
        timeMin = Math.floor((timeInt-timeHour*3600)/60);
        timeSec = timeInt-timeHour*3600-timeMin*60;
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
        time.setText(timeHourString + ":" + timeMinString + ":" + timeSecString);
        price.setText(pref.getString("ParkPrice", null) + " Ft");
        pref = this.getActivity().getSharedPreferences(Constants.RSSI, Context.MODE_PRIVATE);
        final String sms = pref.getString(Constants.SMSBase, "30-763");
        final String licensePlate = pref.getString(Constants.LicensePlate, "ABC-123");

        AppCompatButton pay = (AppCompatButton)finish.findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("SMS");
                alertDialog.setMessage("Parkolás kiegyenlítése SMS küldésével");
                alertDialog.setIcon(R.drawable.ic_parking);

                alertDialog.setPositiveButton("SMS küldés", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + sms));
                        intent.putExtra("sms_body", licensePlate);
                        startActivity(intent);
                    }
                });

                alertDialog.setNegativeButton("Mégsem", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(), "Kérjük, ne felejtse el elhozni beutalóját!", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.show();
            }
        });

        return finish;
    }


}
