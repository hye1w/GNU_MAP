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

            if (selectedBuilding != null) {
                buildingName = selectedBuilding.getBuilding_name();
                buildingNum = String.valueOf(selectedBuilding.getBuilding_num());
                buildingImg = selectedBuilding.getBuilding_img();

                buildingNameTextView.setText(buildingName);

                // Check if the building is already bookmarked
                DatabaseReference newBookmarkRef = databaseReference.child(buildingNum);
                newBookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            bookmarkButton.setImageResource(R.drawable.heart_full);
                        } else {
                            bookmarkButton.setImageResource(R.drawable.heart_empty);
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
                // 즐겨찾기 추가 또는 제거 동작을 수행하는 메소드 호출
                toggleBookmark();
            }
        });


    }



    private void toggleBookmark() {
        if (buildingName != null && buildingNum != null && buildingImg != null) {
            DatabaseReference newBookmarkRef = databaseReference.child(buildingNum);
            newBookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 이미 즐겨찾기에 추가된 경우 삭제
                        dataSnapshot.getRef().removeValue();
                        bookmarkButton.setImageResource(R.drawable.heart_empty); // 이미지 변경
                    } else {
                        // 즐겨찾기에 추가되지 않은 경우 추가
                        newBookmarkRef.child("building_name").setValue(buildingName);
                        newBookmarkRef.child("building_num").setValue(buildingNum);
                        newBookmarkRef.child("building_img").setValue(buildingImg);
                        bookmarkButton.setImageResource(R.drawable.heart_full); // 이미지 변경
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                }
            });
        }
    }

    // 로컬에 즐겨찾기 정보 저장
    private void saveBookmark(Buildings building) {
        // 로컬에 즐겨찾기 정보 저장
        // SharedPreferences 또는 SQLite 등을 활용하여 구현
    }

    // 로컬에서 즐겨찾기 정보 삭제
    private void removeBookmark(Buildings building) {
        // 로컬에서 즐겨찾기 정보 삭제
        // SharedPreferences 또는 SQLite 등을 활용하여 구현
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
