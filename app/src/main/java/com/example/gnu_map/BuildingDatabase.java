package com.example.gnu_map;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Building.class}, version = 1)
public abstract class BuildingDatabase extends RoomDatabase {
    public abstract BuildingDao buildingDao();
}