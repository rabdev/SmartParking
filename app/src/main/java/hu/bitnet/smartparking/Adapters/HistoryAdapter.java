package hu.bitnet.smartparking.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import hu.bitnet.smartparking.Objects.History;
import hu.bitnet.smartparking.R;

import static com.google.android.gms.wearable.DataMap.TAG;
import static java.lang.Long.parseLong;

/**
 * Created by nyulg on 2017. 08. 05..
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<History> android;

    public HistoryAdapter(ArrayList<History> android) {
        this.android = android;
    }


    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int i) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy. MMMM dd. kk:mm");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(parseLong(android.get(i).getSum().getStart()+"000"));
        Log.d(TAG, "calendar: "+calendar);

        viewHolder.tv_address.setText(android.get(i).getPlace().getAddress().toString());
        viewHolder.tv_priceper.setText(android.get(i).getPlace().getPrice().toString()+" Ft/óra");
        viewHolder.tv_price.setText(android.get(i).getSum().getPrice().toString()+ " Ft");
        viewHolder.tv_timestamp.setText(formatter.format(calendar.getTime()).toString());
        viewHolder.tv_time.setText(" ("+android.get(i).getSum().getTime()+ " perc)");
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_address,tv_priceper,tv_price, tv_timestamp, tv_time;
        public ViewHolder(View view) {
            super(view);

            tv_address = (TextView)view.findViewById(R.id.history_address);
            tv_priceper = (TextView)view.findViewById(R.id.history_priceper);
            tv_price = (TextView)view.findViewById(R.id.history_price);
            tv_timestamp = (TextView)view.findViewById(R.id.history_timestamp);
            tv_time= (TextView) view.findViewById(R.id.history_time);

        }
    }
}

