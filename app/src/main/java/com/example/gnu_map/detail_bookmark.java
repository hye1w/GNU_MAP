package com.example.gnu_map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class detail_bookmark extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference bookmarkReference;
    private DatabaseReference buildingDataReference;
    private TextView b_buildingNameTextView;
    private MapView b_mapView1;
    RelativeLayout b_mapViewContainer;

    private ImageButton b_bookmarkButton;
    private boolean b_isBookmarked = false;
    private String b_buildingNum;
    private String b_buildingImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_bookmark);

        // Initialize Firebase database references
        database = FirebaseDatabase.getInstance();
        bookmarkReference = database.getReference("Bookmark");
        buildingDataReference = database.getReference("Buildingdata");

        // Initialize UI components
        b_buildingNameTextView = findViewById(R.id.b_detail_buildingname);
        b_mapView1 = new MapView(this);
        b_mapViewContainer = findViewById(R.id.b_detail_map);
        b_mapViewContainer.addView(b_mapView1);
        b_bookmarkButton = findViewById(R.id.b_bookmarkButton);

        // Back button click listener
        ImageButton b_go_main_bt = findViewById(R.id.b_go_search_bt);
        b_go_main_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detail_bookmark.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Get selected building number from Intent
        Intent intent = getIntent();
        if (intent != null) {
            b_buildingNum = intent.getStringExtra("selected_building_num");

            // Check if the building number is not null
            if (b_buildingNum != null) {
                // Retrieve building information from Bookmark database
                bookmarkReference.child(b_buildingNum).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Building information found in Bookmark database
                            b_buildingImg = dataSnapshot.child("building_img").getValue(String.class);
                            String buildingName = dataSnapshot.child("building_name").getValue(String.class);
                            String buildingNum = dataSnapshot.child("building_num").getValue(String.class);
                            double latitude = dataSnapshot.child("building_x").getValue(Double.class);
                            double longitude = dataSnapshot.child("building_y").getValue(Double.class);

                            Log.d("detailBuildingName", "detail 건물번호: " + buildingNum);
                            Log.d("detailBuildinglocation", "detail 건물 위치: " + latitude + "," + longitude);
                            // Display building name on UI
                            b_buildingNameTextView.setText(buildingName);

                            // Center map on building location
                            b_mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                            b_mapView1.setZoomLevel(1, true);

                            MapPoint MARKER_POINT1 = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                            MapPOIItem marker1 = new MapPOIItem();
                            marker1.setItemName(buildingName);
                            marker1.setTag(0);
                            marker1.setMapPoint(MARKER_POINT1);
                            marker1.setShowDisclosureButtonOnCalloutBalloon(false);
                            marker1.setMarkerType(MapPOIItem.MarkerType.BluePin);

                            b_mapView1.addPOIItem(marker1);


                            // Check if the building is bookmarked
                            b_bookmarkButton.setImageResource(R.drawable.heart_full);
                        } else {
                            // Building information not found in Bookmark database
                            Log.e("Detail 화면", "해당 건물 번호의 즐겨찾기 정보를 찾을 수 없습니다.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Error handling
                        Log.e("Detail 화면", "Firebase에서 즐겨찾기 정보를 가져오는 데 실패했습니다: " + databaseError.getMessage());
                    }
                });



            } else {
                // Building number is null
                Log.e("Detail 화면", "전달된 건물 번호가 null입니다.");
            }
        } else {
            // Intent is null
            Log.e("Detail 화면", "전달된 Intent가 null입니다.");
        }

        // Bookmark button click listener
        b_bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle bookmark status
                toggleBookmark();
            }
        });
    }

    // Toggle bookmark status
    private void toggleBookmark() {
        if (b_buildingNum != null && b_buildingImg != null) {
            DatabaseReference newBookmarkRef = bookmarkReference.child(b_buildingNum);
            newBookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Building is already bookmarked, remove bookmark
                        dataSnapshot.getRef().removeValue();
                        b_bookmarkButton.setImageResource(R.drawable.heart_empty); // Change button icon
                    } else {
                        // Building is not bookmarked, add bookmark
                        newBookmarkRef.child("building_name").setValue(b_buildingNameTextView.getText().toString());
                        newBookmarkRef.child("building_img").setValue(b_buildingImg);
                        b_bookmarkButton.setImageResource(R.drawable.heart_full); // Change button icon
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Error handling
                    Log.e("Detail 화면", "Firebase에서 즐겨찾기 정보를 업데이트하는 데 실패했습니다: " + databaseError.getMessage());
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (b_mapViewContainer != null) {
            b_mapViewContainer.removeAllViews();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        b_mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.15412303, 128.0988896), true); // Center map on default location
        b_mapView1.setZoomLevel(2, true); // Set default zoom level
    }
}
