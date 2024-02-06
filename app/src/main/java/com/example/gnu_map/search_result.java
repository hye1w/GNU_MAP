package com.example.gnu_map;

import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class search_result extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);


        //뒤로가기 버튼
        ImageButton go_main_bt = (ImageButton) findViewById(R.id.go_main_bt);

        go_main_bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                // MainActivity로 이동하는 Intent 생성
                Intent intent = new Intent(search_result.this, MainActivity.class);

                // Intent를 사용하여 MainActivity로 이동
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
        }





        CustomAdapter adapter = new CustomAdapter(searchResultsList, this);
        recyclerView.setAdapter(adapter);

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
