package hu.bitnet.smartparking.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hu.bitnet.smartparking.Objects.Parking_places;
import hu.bitnet.smartparking.R;

/**
 * Created by nyulg on 2017. 08. 05..
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList<Parking_places> android;

    public SearchAdapter(ArrayList<Parking_places> android) {
        this.android = android;
    }


    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_address.setText(android.get(i).getAddress());
        viewHolder.tv_priceper.setText(android.get(i).getPrice());
        viewHolder.tv_km.setText(android.get(i).getDistance()+" km");
        viewHolder.tv_traffic.setText(android.get(i).getTime()+" min without traffix");
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_km, tv_priceper, tv_address, tv_traffic;

        public ViewHolder(View view) {
            super(view);

            tv_address = (TextView) view.findViewById(R.id.search_address);
            tv_priceper = (TextView) view.findViewById(R.id.search_priceper);
            tv_km = (TextView) view.findViewById(R.id.search_km);
            tv_traffic = (TextView) view.findViewById(R.id.search_traffic);

        }
    }
}