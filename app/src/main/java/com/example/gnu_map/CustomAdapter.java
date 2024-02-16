package com.example.gnu_map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Buildings> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Buildings> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }



    // 추가
    public interface OnItemClickListener {
        void onItemClick(Buildings building);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Buildings building = arrayList.get(position);
        if (building.getBuilding_num() != 1000 && building.getBuilding_num() != 1001 && building.getBuilding_num() != 1002) { // building_num이 1000이 아닌 경우
            Glide.with(holder.itemView)
                    .load(building.getBuilding_img())
                    .into(holder.building_img);
            holder.buildingname_text.setText(building.getBuilding_name());
            holder.buildingnum_text.setText(String.valueOf(building.getBuilding_num() + "동"));
        } else { // building_num이 1000인 경우
            Glide.with(holder.itemView)
                    .load(building.getBuilding_img())
                    .into(holder.building_img);
            holder.buildingname_text.setText(building.getBuilding_name());
            holder.buildingnum_text.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(building);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView building_img;
        TextView buildingname_text;
        TextView buildingnum_text;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.building_img = itemView.findViewById(R.id.building_img);
            this.buildingname_text = itemView.findViewById(R.id.buildingname_text);
            this.buildingnum_text = itemView.findViewById(R.id.buildingnum_text);
        }
    }
}
