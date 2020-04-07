package com.example.beatthevirus;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
List<list_Info> list;

    public ListAdapter(List<list_Info> list, Context context){
        this.list=list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
    TextView bluetooth, distance;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            bluetooth=itemView.findViewById(R.id.blutooth);
            distance = itemView.findViewById((R.id.distance));
        }
    }

    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_unit,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bluetooth.setText(list.get(position).getBlutooth());
        if(list.get(position).getDistance()<2.5)
        holder.distance.setTextColor(Color.RED);
        else
            holder.distance.setTextColor(Color.BLACK);

        holder.distance.setText(String.valueOf(list.get(position).getDistance()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

