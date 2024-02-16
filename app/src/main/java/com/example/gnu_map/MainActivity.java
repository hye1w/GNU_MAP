package com.example.gnu_map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;

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
import android.widget.Spinner;
import android.widget.TextView;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity {

    //    //현재위치
////    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FirebaseDatabase database;
    //    private DatabaseReference databaseReference;
    private DatabaseReference GajwaReference;
    private DatabaseReference ChilamReference;
    private DatabaseReference TongyeongReference;
    private DatabaseReference ChangwonReference;
    private DatabaseReference NaedongReference;



    private SearchView searchView;
    private MapView mapView1;
    RelativeLayout mapViewContainer1;


    private Spinner campusSpinner;
    private ArrayAdapter<CharSequence> campusAdapter;

    private RecyclerView bookmarkRecyclerView;
    private List<Bookmark> bookmarkList;
    private BookmarkAdapter adapter;
    private TextView noBookmarkView;

    private String selectedCampus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // 현재 사용자 가져오기
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String uid = currentUser.getUid();

        // Firebase 데이터베이스 초기화
        database = FirebaseDatabase.getInstance();
        DatabaseReference bookmarkRef = database.getReference("Bookmark").child(uid);

        database = FirebaseDatabase.getInstance();
        GajwaReference = database.getReference("GajwaBuilding");
        ChilamReference = database.getReference("ChilamBuilding");
        TongyeongReference = database.getReference("TongyeongBuilding");
        ChangwonReference = database.getReference("ChangwonBuilding");
        NaedongReference = database.getReference("NaedongBuilding");

        // Spinner 초기화
        campusSpinner = findViewById(R.id.campus_spinner);

        // 캠퍼스 목록 가져오기
        CharSequence[] campuses = getResources().getTextArray(R.array.campus_array);

        // ArrayAdapter 생성
        campusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, campuses);
        campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        campusSpinner.setAdapter(campusAdapter);

        campusSpinner.setSelection(0); // "가좌캠퍼스"가 기본 선택

        // Spinner 아이템 선택 리스너 설정
        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 항목의 위치(position) 또는 값(campuses[position])을 사용하여 원하는 작업 수행
                selectedCampus = parent.getItemAtPosition(position).toString();

                if (selectedCampus.equals("가좌캠퍼스")) {
                    mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.15412303, 128.0988896), true);
                    mapView1.setZoomLevel(2, true); // 필요한 경우 지도 줌 레벨도 설정
                }
                else if (selectedCampus.equals("칠암캠퍼스")) {
                    mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.180721, 128.094039), true);
                    mapView1.setZoomLevel(2, true); // 필요한 경우 지도 줌 레벨도 설정
                }
                else if (selectedCampus.equals("통영캠퍼스")) {
                    mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(34.838687, 128.399653), true);
                    mapView1.setZoomLevel(2, true); // 필요한 경우 지도 줌 레벨도 설정
                }
                else if (selectedCampus.equals("창원산학캠퍼스")) {
                    mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.240670, 128.631700), true);
                    mapView1.setZoomLevel(2, true); // 필요한 경우 지도 줌 레벨도 설정
                }
                else if (selectedCampus.equals("내동캠퍼스")) {
                    mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.155678, 128.083143), true);
                    mapView1.setZoomLevel(2, true); // 필요한 경우 지도 줌 레벨도 설정
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 것도 선택되지 않았을 때의 동작
            }
        });

        noBookmarkView = findViewById(R.id.no_bookmark_textview);

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

        Button locationButton = findViewById(R.id.locationbutton);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCampus = campusSpinner.getSelectedItem().toString();
                switch (selectedCampus) {
                    case "가좌캠퍼스":
                        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.15412303, 128.0988896), true);
                        mapView1.setZoomLevel(2, true);
                        break;
                    case "칠암캠퍼스":
                        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.180721, 128.094039), true);
                        mapView1.setZoomLevel(2, true);
                        break;
                    case "통영캠퍼스":
                        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(34.838687, 128.399653), true);
                        mapView1.setZoomLevel(2, true);
                        break;
                    case "창원산학캠퍼스":
                        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.240670, 128.631700), true);
                        mapView1.setZoomLevel(2, true);
                        break;
                    case "내동캠퍼스":
                        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.155678, 128.083143), true);
                        mapView1.setZoomLevel(2, true);
                        break;
                    default:
                        // 기본적으로 가좌캠퍼스로 설정
                        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.15412303, 128.0988896), true);
                        mapView1.setZoomLevel(2, true);
                        break;
                }
            }
        });




        bookmarkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookmarkList.clear();
                for (DataSnapshot campusSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot buildingSnapshot : campusSnapshot.getChildren()) {
                        String buildingImg = buildingSnapshot.child("building_img").getValue(String.class);
                        String buildingNum = buildingSnapshot.child("building_num").getValue(String.class);
                        String buildingName = buildingSnapshot.child("building_name").getValue(String.class);
                        String campus = buildingSnapshot.child("campus").getValue(String.class);

                        Bookmark bookmark = new Bookmark(buildingImg, buildingNum, buildingName, campus);
                        bookmarkList.add(bookmark);
                    }
                }

                adapter.notifyDataSetChanged();

                if (dataSnapshot.exists()) {
                    Log.d("즐겨찾기text", "즐겨찾기 목록 있음");
                    noBookmarkView.setVisibility(View.GONE);
                    bookmarkRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    Log.d("즐겨찾기text", "즐겨찾기 목록 없음");
                    noBookmarkView.setVisibility(View.VISIBLE);
                    bookmarkRecyclerView.setVisibility(View.GONE);
                }

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
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Buildingdata");

        DatabaseReference selectedReference;
        String selectedCampus = campusSpinner.getSelectedItem().toString();

        // 현재 선택된 캠퍼스에 따라 데이터베이스 선택
        if (selectedCampus.equals("가좌캠퍼스")) {
            selectedReference = GajwaReference;
        } else if (selectedCampus.equals("칠암캠퍼스")) {
            selectedReference = ChilamReference;
        } else if (selectedCampus.equals("통영캠퍼스")) {
            selectedReference = TongyeongReference;
        } else if (selectedCampus.equals("창원산학캠퍼스")) {
            selectedReference = ChangwonReference;
        } else if (selectedCampus.equals("내동캠퍼스")) {
            selectedReference = NaedongReference;
        } else {
            selectedReference = GajwaReference;
        }

        Query queryByName = selectedReference.orderByChild("building_name").startAt(searchText).endAt(searchText + "\uf8ff");

        queryByName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Buildings> searchResultsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Buildings building = snapshot.getValue(Buildings.class);
                    searchResultsList.add(building);
                }

                // 여기서 검색 결과에 building_num으로 숫자가 포함되는 결과 추가
                selectedReference.orderByChild("building_num").addListenerForSingleValueEvent(new ValueEventListener() {
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

    public String getSelectedCampus() {
        return selectedCampus;
    }
}