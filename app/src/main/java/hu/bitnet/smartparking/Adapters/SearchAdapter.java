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
    private static SearchAdapter.ClickListener clickListener;

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
        viewHolder.tv_priceper.setText(String.format("%.0f", Double.parseDouble(android.get(i).getPrice())) + " Ft/óra");
        viewHolder.tv_km.setText(String.format("%.1f", Double.parseDouble(android.get(i).getDistance()))+" km");
        viewHolder.tv_traffic.setText(String.format("%.1f", Double.parseDouble(android.get(i).getTime())/60.0)+" min without traffic");
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView tv_km, tv_priceper, tv_address, tv_traffic;

        public ViewHolder(View view) {
            super(view);

            tv_address = (TextView) view.findViewById(R.id.search_address);
            tv_priceper = (TextView) view.findViewById(R.id.search_priceper);
            tv_km = (TextView) view.findViewById(R.id.search_km);
            tv_traffic = (TextView) view.findViewById(R.id.search_traffic);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public void setOnItemClickListener(SearchAdapter.ClickListener clickListener) {
        SearchAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}