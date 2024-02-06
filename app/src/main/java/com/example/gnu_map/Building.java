package com.example.gnu_map;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Building implements Parcelable{

    @PrimaryKey
    public int id;  //하나의 사용자에 대한 고유 ID값
    public String building_name;
    public String building_num;
    public Double building_x;
    public Double building_y;

    private String building_img;

    public Building(int id, String building_name, String building_num, Double building_x, Double building_y, String building_img) {
        this.id = id;
        this.building_name = building_name;
        this.building_num = building_num;
        this.building_x = building_x;
        this.building_y = building_y;
        this.building_img = building_img;
    }

    // Getter 메서드
    public int getId() {
        return id;
    }

    public String getBuildingName() {
        return building_name;
    }

    public String getBuildingNum() {
        return building_num;
    }

    public Double getBuildingX() { return building_x; }

    public Double getBuildingY() {
        return building_y;
    }

    public String getBuilding_img() { return building_img; }

    // Setter 메서드
    public void setId(int id) {
        this.id = id;
    }

    public void setBuildingName(String building_name) {
        this.building_name = building_name;
    }

    public void setBuildingNum(String building_num) {
        this.building_num = building_num;
    }

    public void setBuildingX(Double building_x) {
        this.building_x = building_x;
    }

    public void setBuildingY(Double building_y) { this.building_y = building_y; }

    public void setBuilding_img(String building_img) { this.building_img = building_img; }


    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", buildingName='" + building_name + '\'' +
                ", buildingNum='" + building_num + '\'' +
                ", buildingX=" + building_x +
                ", buildingY=" + building_y +
                ", buildingImg='" + building_img + '\'' +
                '}';
    }

    protected Building(Parcel in) {
        id = in.readInt();
        building_name = in.readString();
        building_num = in.readString();
        building_x = in.readDouble();
        building_y = in.readDouble();
        building_img = in.readString();
    }

    public static final Creator<Building> CREATOR = new Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel in) {
            return new Building(in);
        }

        @Override
        public Building[] newArray(int size) {
            return new Building[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(building_name);
        dest.writeString(building_num);
        dest.writeDouble(building_x);
        dest.writeDouble(building_y);
        dest.writeString(building_img);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
