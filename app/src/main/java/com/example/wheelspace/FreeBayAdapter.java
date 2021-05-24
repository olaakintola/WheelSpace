package com.example.wheelspace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Callback;

public class FreeBayAdapter extends RecyclerView.Adapter<FreeBayAdapter.BayViewHolder> {
    ArrayList<UserPost> futureFreeBayList;
    Context context;

    public FreeBayAdapter(){

    }

    public void setData( ArrayList<UserPost> futureFreeBayList) {
        this.futureFreeBayList = futureFreeBayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FreeBayAdapter.BayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.free_bay_bus_list, parent, false);
        return new FreeBayAdapter.BayViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FreeBayAdapter.BayViewHolder holder, int position) {
        holder.initialiseFreeBusBayVariables(futureFreeBayList.get(position).getRoute(), futureFreeBayList.get(position).getTimes() );
    }

    @Override
    public int getItemCount() {
        return futureFreeBayList.size();
    }

    public class BayViewHolder extends RecyclerView.ViewHolder{

        View freeBusBayView;

        public BayViewHolder(@NonNull View itemView) {
            super(itemView);
            freeBusBayView = itemView;
        }

        public void initialiseFreeBusBayVariables(String futureBusRoute, String futureDepartureTime){
            TextView future_bus_route = freeBusBayView.findViewById(R.id.future_bus_route);
            TextView future_bus_time = freeBusBayView.findViewById(R.id.future_bus_time);

            future_bus_route.setText(futureBusRoute);
            future_bus_time.setText(futureDepartureTime);
        }

    }
}
