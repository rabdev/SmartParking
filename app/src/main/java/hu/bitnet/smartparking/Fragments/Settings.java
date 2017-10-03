package hu.bitnet.smartparking.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import hu.bitnet.smartparking.Objects.Constants;
import hu.bitnet.smartparking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    SharedPreferences pref;
    EditText rssi, licenseplate, et_lp, et_smsbase, et_distance;
    TextView tv_message;
    AlertDialog dialog;
    int prog;
    SeekBar seekBar;
    ProgressBar progress;

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
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.appbar_left);
        imageView.setVisibility(View.GONE);
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.appbar_right);
        imageView1.setVisibility(View.GONE);


        seekBar = (SeekBar) settings.findViewById(R.id.rssi_seek);
        seekBar.setMax(110);
        rssi = (EditText) settings.findViewById(R.id.rssi_settings);
        licenseplate = (EditText) settings.findViewById(R.id.settings_licenseplate);
        et_distance = (EditText) settings.findViewById(R.id.settings_distance);
        et_smsbase = (EditText) settings.findViewById(R.id.settings_smsbase);

        pref = this.getActivity().getSharedPreferences(Constants.RSSI, Context.MODE_PRIVATE);
        String rssi1=pref.getString(Constants.RSSI,null);
        String licenseplate1 = pref.getString(Constants.LicensePlate,null);
        final String distance = pref.getString(Constants.SettingsDistance,null);
        String smsbase = pref.getString(Constants.SMSBase,null);

        if (licenseplate1==null|| licenseplate1.isEmpty() || licenseplate1.equals("")){
            licenseplate.setText("");
            showDialog();
        } else {
            licenseplate.setText(pref.getString(Constants.LicensePlate,""));
        }

        if (distance==null){
            et_distance.setText("2500 m");
        } else {
            et_distance.setText(pref.getString(Constants.SettingsDistance,""));
        }

        if (smsbase==null) {
            et_smsbase.setText("30-763");
        } else {
            et_smsbase.setText(pref.getString(Constants.SMSBase,""));
        }

        if (rssi1==null) {
            prog=-95+150;
            seekBar.setProgress(prog);
            rssi.setText("-95");
        }   else {
            prog = Integer.parseInt(pref.getString(Constants.RSSI,"")) + 150;
            seekBar.setProgress(prog);
            rssi.setText(pref.getString(Constants.RSSI,""));
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = seekBar.getProgress();
                prog=progress-150;
                rssi.setText(String.valueOf(prog));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AppCompatButton save = (AppCompatButton) settings.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rssi2,licenseplate2, smsbase2, distance2;
                rssi2=rssi.getText().toString();
                licenseplate2=licenseplate.getText().toString();
                distance2=et_distance.getText().toString();
                smsbase2=et_smsbase.getText().toString();

                SharedPreferences.Editor editor= pref.edit();
                editor.putString(Constants.RSSI,rssi2);
                editor.putString(Constants.LicensePlate,licenseplate2);
                editor.putString(Constants.SettingsDistance,distance2);
                editor.putString(Constants.SMSBase,smsbase2);
                editor.apply();

                Toast.makeText(getContext(), "Adatok mentve", Toast.LENGTH_LONG).show();
            }
        });


        return settings;
    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_license, null);
        et_lp = (EditText)view.findViewById(R.id.et_new_lp);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Set License Plate");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_lp1 = et_lp.getText().toString();
                if(!et_lp1.isEmpty()){

                    progress.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.LicensePlate,et_lp1);
                    editor.apply();
                    licenseplate.setText(et_lp1);
                    progress.setVisibility(View.GONE);
                    dialog.dismiss();

                }else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

}
