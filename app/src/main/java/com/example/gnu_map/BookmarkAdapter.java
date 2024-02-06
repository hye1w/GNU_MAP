package com.example.gnu_map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private List<Bookmark> bookmarkList;

    BookmarkAdapter(List<Bookmark> bookmarkList) {
        this.bookmarkList = bookmarkList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(bookmarkList.get(position).getBuildingImg())
                .into(holder.building_img);
        holder.buildingNameTextView.setText(bookmarkList.get(position).getBuildingName());
        holder.buildingNumTextView.setText(String.valueOf(bookmarkList.get(position).getBuildingNum() + "동"));
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView building_img;
        private TextView buildingNameTextView;
        private TextView buildingNumTextView;

        ViewHolder(View itemView) {
            super(itemView);
            this.building_img = itemView.findViewById(R.id.building_img);
            this.buildingNameTextView = itemView.findViewById(R.id.buildingname_text);
            this.buildingNumTextView = itemView.findViewById(R.id.buildingnum_text);
        }

        void bind(Bookmark bookmark) {
            buildingNameTextView.setText(bookmark.getBuildingName());
            buildingNumTextView.setText(bookmark.getBuildingNum());
        }
    }
}
