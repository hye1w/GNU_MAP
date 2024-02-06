package com.example.gnu_map;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BuildingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // 데이터 중복 시 무시
    void insertBuilding(Building building);

    @Insert(onConflict = OnConflictStrategy.IGNORE) // 데이터 중복 시 무시
    void insertAll(List<Building> buildings);


    @Query("SELECT * FROM BUILDING")
    List<Building> getBuildingAll();

    @Query("SELECT * FROM BUILDING WHERE building_name LIKE '%' || :query || '%' OR building_num LIKE '%' || :query || '%'")
    List<Building> searchBuilding(String query);

    @Query("DELETE FROM BUILDING")
    void deleteAllBuildings();

}
