package com.example.gnu_map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.RelativeLayout;
import android.widget.TextView;

import net.daum.android.map.location.MapViewLocationManager;
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
//    private ViewGroup mapViewContainer1;
//    private double currentLatitude;
//    private double currentLongitude;

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

        // 즐겨찾기 버튼 클릭 시 동작
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBookmark();
            }
        });

    }

//    private void toggleBookmark() {
//        if (buildingName != null && buildingNum != null && buildingImg != null) {
//            DatabaseReference newBookmarkRef = databaseReference.child(buildingNum);
//
//            newBookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String bookmarkedCampus = dataSnapshot.child("campus").getValue(String.class);
//                        if (campus.equals(bookmarkedCampus)) {
//                            // 현재 선택된 캠퍼스와 즐겨찾기에 추가된 건물의 캠퍼스가 일치하는 경우에만 즐겨찾기 삭제
//                            dataSnapshot.getRef().removeValue();
//                            bookmarkButton.setImageResource(R.drawable.heart_empty); // 이미지 변경
//                        } else {
//                            // 현재 선택된 캠퍼스와 즐겨찾기에 추가된 건물의 캠퍼스가 일치하지 않으면 안내 메시지 또는 다른 처리 수행
//                            // 여기에 처리를 추가하세요
//                        }
//                    } else {
//                        // 즐겨찾기에 추가되지 않은 경우 추가
//                        newBookmarkRef.child("building_name").setValue(buildingName);
//                        newBookmarkRef.child("building_num").setValue(buildingNum);
//                        newBookmarkRef.child("building_img").setValue(buildingImg);
//                        newBookmarkRef.child("building_x").setValue(buildingX);
//                        newBookmarkRef.child("building_y").setValue(buildingY);
//                        newBookmarkRef.child("campus").setValue(campus);
//                        bookmarkButton.setImageResource(R.drawable.heart_full); // 이미지 변경
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // 에러 처리
//                }
//            });
//        }
//    }



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
            // 여기에 처리를 추가하세요
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
        // 지도가 이미 추가되어 있는지 확인하고 추가되어 있지 않으면 다시 추가
        if (mapViewContainer.getChildCount() == 0) {
            mapViewContainer.addView(mapView1);
        }
    }


//    @Override
//    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float v) {
//        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
//        currentLatitude = mapPointGeo.latitude;
//        currentLongitude = mapPointGeo.longitude;
//    }
//
//    @Override
//    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
//
//    }
//
//    @Override
//    public void onCurrentLocationUpdateFailed(MapView mapView) {
//
//    }
//
//    @Override
//    public void onCurrentLocationUpdateCancelled(MapView mapView) {
//
//    }
//
//    @Override
//    public void onMapViewInitialized(MapView mapView) {
//
//    }
//
//    @Override
//    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
//
//    }
//
//    @Override
//    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
//
//    }









}