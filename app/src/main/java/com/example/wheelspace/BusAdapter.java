package com.example.wheelspace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {

    ArrayList<WheelBayStatus> busList;
    Context context;

    public BusAdapter(Context context, ArrayList<WheelBayStatus> busList){
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bus_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WheelBayStatus wheelBayStatus = busList.get(position);
        holder.route_number.setText(wheelBayStatus.getRoute() );
        holder.bus_time.setText(wheelBayStatus.getTime() );
//        holder.availability_icon()
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView route_number, bus_time;
        ImageView availability_icon;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            route_number = itemView.findViewById(R.id.route_number);
            bus_time = itemView.findViewById(R.id.bus_time);
//            availability_icon = itemView.findViewById(R.id.availability_icon);
        }
    }
}
