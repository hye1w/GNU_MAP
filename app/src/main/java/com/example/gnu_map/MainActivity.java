package com.example.gnu_map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity {
    //    private RecyclerView recyclerView;
//    private RecyclerView.Adapter adapter;
//    private RecyclerView.LayoutManager layoutManager;
//    private ArrayList<Buildings> arrayList;

    //    //현재위치
////    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    private SearchView searchView;
    private MapView mapView1;
    RelativeLayout mapViewContainer1;



    private RecyclerView bookmarkRecyclerView;
    private List<Bookmark> bookmarkList;
    private BookmarkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SearchView 초기화
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 사용자가 검색 버튼을 눌렀을 때 호출되는 부분
                Log.d("SearchQuery", "Submitted Query: " + query);
                searchBuilding(query); // 검색어를 이용하여 건물 검색
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색어가 변경될 때마다 호출되는 부분
                return false;
            }
        });


        //지도
        mapView1 = new MapView(this);

        mapViewContainer1 = (RelativeLayout) findViewById(R.id.main_map);
        mapViewContainer1.addView(mapView1);

        // 중심점 변경
        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.15412303, 128.0988896), true);
        // 줌 인
        mapView1.zoomIn(true);

        // 줌 아웃
        mapView1.zoomOut(true);

        bookmarkRecyclerView = findViewById(R.id.bookmark_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);


        bookmarkRecyclerView.setLayoutManager(layoutManager);
        bookmarkRecyclerView.setHasFixedSize(true);

        bookmarkList = new ArrayList<>();
        adapter = new BookmarkAdapter(bookmarkList);
        bookmarkRecyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookmarkRef = database.getReference("Bookmark");

        Button locationButton = findViewById(R.id.locationbutton);

        // locationbutton 버튼 클릭 리스너 설정
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼이 클릭되었을 때 호출될 메서드 호출
                setLocation();
            }
        });


        bookmarkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookmarkList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String buildingImg = snapshot.child("building_img").getValue(String.class);
                    String buildingNum = snapshot.child("building_num").getValue(String.class);
                    String buildingName = snapshot.child("building_name").getValue(String.class);

                    Bookmark bookmark = new Bookmark(buildingImg, buildingNum, buildingName);
                    bookmarkList.add(bookmark);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터를 불러오지 못한 경우의 처리
            }
        });
    }

    // GNU버튼 지도 위치 리셋
    private void setLocation() {
        // 지도의 중심을 변경
        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.15412303, 128.0988896), true);
        mapView1.setZoomLevel(2, true);
    }


    private void searchBuilding(String searchText) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Buildingdata");

        Query queryByName = databaseReference.orderByChild("building_name").startAt(searchText).endAt(searchText + "\uf8ff");

        queryByName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Buildings> searchResultsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Buildings building = snapshot.getValue(Buildings.class);
                    searchResultsList.add(building);
                }

                // 여기서 검색 결과에 building_num으로 숫자가 포함되는 결과 추가
                databaseReference.orderByChild("building_num").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Buildings building = snapshot.getValue(Buildings.class);
                            if (String.valueOf(building.getBuilding_num()).equals(searchText)) {
                                searchResultsList.add(building);
                            }
                        }

                        // 검색 결과 페이지로 이동하고 검색 결과 리스트를 전달
                        Intent intent = new Intent(MainActivity.this, search_result.class);
                        intent.putParcelableArrayListExtra("searchResultsList", searchResultsList);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("MainActivity", "Error searching buildings by number: " + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MainActivity", "Error searching buildings by name: " + databaseError.getMessage());
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapViewContainer1 != null) {
            mapViewContainer1.removeAllViews();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.15412303, 128.0988896), true); // 맵의 중심점 설정
        mapView1.setZoomLevel(2, true); // 맵의 줌 레벨 설정
    }


}