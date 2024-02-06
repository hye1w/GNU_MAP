package com.example.gnu_map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder> {
    private List<Building> buildingList;

    public BuildingAdapter(List<Building> buildingList) {
        this.buildingList = buildingList;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list, parent, false);
        return new BuildingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        Building building = buildingList.get(position);

        Glide.with(holder.itemView)
                .load(building.getBuilding_img())
                .into(holder.building_img);
        holder.buildingNameTextView.setText(building.getBuildingName());
        holder.buildingNumTextView.setText(String.valueOf(building.getBuildingNum() + "동"));


    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }

    static class BuildingViewHolder extends RecyclerView.ViewHolder {
        ImageView building_img;
        TextView buildingNameTextView;
        TextView buildingNumTextView;

        BuildingViewHolder(View itemView) {
            super(itemView);
            this.building_img = itemView.findViewById(R.id.building_img);
            buildingNameTextView = itemView.findViewById(R.id.buildingname_text);
            buildingNumTextView = itemView.findViewById(R.id.buildingnum_text);
        }
    }
}