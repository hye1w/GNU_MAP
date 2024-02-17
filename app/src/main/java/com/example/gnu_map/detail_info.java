package com.example.gnu_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.RelativeLayout;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.widget.Toast;
import android.view.ViewGroup;


public class detail_info extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private TextView buildingNameTextView;
    private MapView mapView1;
    RelativeLayout mapViewContainer;

    private ImageButton bookmarkButton;
    private boolean isBookmarked = false;
    private String buildingName;
    private String buildingNum;
    private String buildingImg;
    private double buildingX;
    private double buildingY;
    private String campus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_info);

        //뒤로가기 버튼
        ImageButton go_main_bt = (ImageButton) findViewById(R.id.go_search_bt);

        go_main_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //지도 해당 위치 표시
        buildingNameTextView = findViewById(R.id.detail_buildingname);

        mapView1 = new MapView(this);

        mapViewContainer = (RelativeLayout) findViewById(R.id.detail_map);
        mapViewContainer.addView(mapView1);

        bookmarkButton = findViewById(R.id.bookmarkButton);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Bookmark");

        Intent intent = getIntent();
        if (intent != null) {
            Buildings selectedBuilding = intent.getParcelableExtra("selected_building");
            campus = intent.getStringExtra("selected_campus");

            if (selectedBuilding != null) {
                buildingName = selectedBuilding.getBuilding_name();
                buildingNum = String.valueOf(selectedBuilding.getBuilding_num());
                buildingImg = selectedBuilding.getBuilding_img();
                buildingX = selectedBuilding.getBuilding_x();
                buildingY = selectedBuilding.getBuilding_y();
                campus = selectedBuilding.getCampus();

                buildingNameTextView.setText(buildingName);

                // Check if the building is already bookmarked
                DatabaseReference newBookmarkRef = databaseReference.child(campus).child(buildingNum);
                newBookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            bookmarkButton.setImageResource(R.drawable.heart_full);
                            isBookmarked = true;
                        } else {
                            bookmarkButton.setImageResource(R.drawable.heart_empty);
                            isBookmarked = false;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Error handling
                    }
                });

                buildingNameTextView.setText(selectedBuilding.getBuilding_name());
                String name = selectedBuilding.getBuilding_name();

                double latitude = selectedBuilding.getBuilding_x();
                double longitude = selectedBuilding.getBuilding_y();
                mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                mapView1.setZoomLevel(1, true);

                MapPoint MARKER_POINT1 = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                MapPOIItem marker1 = new MapPOIItem();
                marker1.setItemName(name);
                marker1.setTag(0);
                marker1.setMapPoint(MARKER_POINT1);
                marker1.setShowDisclosureButtonOnCalloutBalloon(false);
                marker1.setMarkerType(MapPOIItem.MarkerType.BluePin);

                mapView1.addPOIItem(marker1);
            }
        }

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBookmark();
            }
        });

    }

    private void toggleBookmark() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference newBookmarkRef = databaseReference.child(uid).child(campus).child(buildingNum);

            newBookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 즐겨찾기가 이미 존재하면 삭제
                        newBookmarkRef.removeValue();
                        bookmarkButton.setImageResource(R.drawable.heart_empty);
                        isBookmarked = false;
                    } else {
                        // 즐겨찾기 추가
                        newBookmarkRef.child("building_name").setValue(buildingName);
                        newBookmarkRef.child("building_num").setValue(buildingNum);
                        newBookmarkRef.child("building_img").setValue(buildingImg);
                        newBookmarkRef.child("building_x").setValue(buildingX);
                        newBookmarkRef.child("building_y").setValue(buildingY);
                        newBookmarkRef.child("campus").setValue(campus);
                        newBookmarkRef.child("uid").setValue(uid);
                        bookmarkButton.setImageResource(R.drawable.heart_full);
                        isBookmarked = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                }
            });
        } else {
            // 사용자가 로그인되어 있지 않은 경우에 대한 처리
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapViewContainer != null) {
            mapViewContainer.removeAllViews();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 지도가 이미 추가되어 있는지 확인하고 추가되어 있지 않으면 추가
        if (mapViewContainer.getChildCount() == 0) {
            mapViewContainer.addView(mapView1);
        }
    }
}