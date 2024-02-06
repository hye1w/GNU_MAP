package com.example.gnu_map;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BuildingRepository {
    private BuildingDao buildingDao;
    private BuildingDatabase database;

    public BuildingRepository(Context context) {
        database = Room.databaseBuilder(context, BuildingDatabase.class, "building_1")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        buildingDao = database.buildingDao();
    }

    public void insertBuildingsFromCSV(Context context) {
        // CSV 파일을 읽어오기 위한 InputStream
        InputStream inputStream = context.getResources().openRawResource(R.raw.building_data);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // 헤더 라인을 무시
            reader.readLine();

            List<Building> buildingList = new ArrayList<>();

            // CSV 파일에서 데이터를 읽어와서 Building 객체를 생성하고 리스트에 추가
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    int id = Integer.parseInt(data[0]);
                    String buildingName = data[1];
                    String buildingNum = data[2];
                    Double buildingX = Double.parseDouble(data[3]);
                    Double buildingY = Double.parseDouble(data[4]);
                    String buildingImg = data[5];

                    Log.d("CSV Data", "ID: " + id + ", Name: " + buildingName + ", Num: " + buildingNum +
                            ", X: " + buildingX + ", Y: " + buildingY + ", Img: " + buildingImg);

                    buildingList.add(new Building(id, buildingName, buildingNum, buildingX, buildingY, buildingImg));

                }
            }

//            // Room Database에 데이터 추가
//            if (!buildingList.isEmpty()) {
//                database.runInTransaction(new Runnable() {
//                    @Override
//                    public void run() {
//                        buildingDao.insertAll(buildingList);
//                        Log.d("BuildingRepository", "Data inserted into Room Database.");
//                    }
//                });
//            }


        buildingDao.insertAll(buildingList);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the database explicitly after use
            database.close();
        }


    }
}