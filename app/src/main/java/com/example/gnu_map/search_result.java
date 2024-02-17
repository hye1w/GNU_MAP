package com.example.gnu_map;

import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class search_result extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        // "정보가 없습니다." 텍스트를 나타내는 TextView 초기화
        noDataTextView = findViewById(R.id.no_data_textview);

        //뒤로가기 버튼
        ImageButton go_main_bt = (ImageButton) findViewById(R.id.go_main_bt);

        go_main_bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(search_result.this, MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Buildings> searchResultsList = getIntent().getParcelableArrayListExtra("searchResultsList");

        if (searchResultsList != null) {
            adapter = new CustomAdapter(searchResultsList, this);
            recyclerView.setAdapter(adapter);
            Log.d("INFO_TAG", "검색된 정보의 개수: " + adapter.getItemCount());

            if(searchResultsList.size() == 0)
            {
                noDataTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Log.d("INFO_TAG", "검색된 정보 없음: " + adapter.getItemCount());
            }
        }

        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Buildings building) {
                Intent intent = new Intent(search_result.this, detail_info.class);
                intent.putExtra("selected_building", building);
                startActivity(intent);
            }
        });
    }
}
