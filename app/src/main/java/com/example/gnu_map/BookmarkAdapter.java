package com.example.gnu_map;

import android.content.Intent;
import android.util.Log;
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
        Bookmark bookmark = bookmarkList.get(position);
        holder.bind(bookmark);
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView building_img;
        private TextView buildingNameTextView;
        private TextView buildingNumTextView;

        ViewHolder(View itemView) {
            super(itemView);
            this.building_img = itemView.findViewById(R.id.building_img);
            this.buildingNameTextView = itemView.findViewById(R.id.buildingname_text);
            this.buildingNumTextView = itemView.findViewById(R.id.buildingnum_text);

            // 클릭 리스너 설정
            itemView.setOnClickListener(this);
        }

        void bind(Bookmark bookmark) {
            Glide.with(itemView)
                    .load(bookmark.getBuildingImg())
                    .into(building_img);
            buildingNameTextView.setText(bookmark.getBuildingName());
            buildingNumTextView.setText(String.valueOf(bookmark.getBuildingNum() + "동"));
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // 선택된 항목의 건물명 가져오기
                String selectedBuildingName = bookmarkList.get(position).getBuildingName();
                String selectedBuildingNum = bookmarkList.get(position).getBuildingNum();
                // Logcat에 출력
                Log.d("SelectedBuildingName", "선택된 건물명: " + selectedBuildingName);
                Log.d("SelectedBuildingNum", "선택된 건물번호: " + selectedBuildingNum);

                // Intent를 통해 detail_bookmark 화면으로 건물 번호 전달
                Intent intent = new Intent(view.getContext(), detail_bookmark.class);
                intent.putExtra("selected_building_num", selectedBuildingNum);
                view.getContext().startActivity(intent);
            }
        }
    }
}
