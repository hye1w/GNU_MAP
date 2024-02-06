package com.example.gnu_map;

import android.os.Parcel;
import android.os.Parcelable;

public class Buildings implements Parcelable {

    private String building_name;
    private int building_num;
    private Double building_x;
    private Double building_y;
    private String building_img;



    public Buildings() {
    }

    public Buildings(Parcel in) {
        building_name = in.readString();
        building_num = in.readInt();
        if (in.readByte() == 0) {
            building_x = null;
        } else {
            building_x = in.readDouble();
        }
        if (in.readByte() == 0) {
            building_y = null;
        } else {
            building_y = in.readDouble();
        }
        building_img = in.readString();
    }


    public static final Creator<Buildings> CREATOR = new Creator<Buildings>() {
        @Override
        public Buildings createFromParcel(Parcel in) {
            return new Buildings(in);
        }

        @Override
        public Buildings[] newArray(int size) {
            return new Buildings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(building_name);
        dest.writeInt(building_num);
        if (building_x == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(building_x);
        }
        if (building_y == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(building_y);
        }
        dest.writeString(building_img);
    }


    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public int getBuilding_num() {
        return building_num;
    }

    public void setBuilding_num(int building_num) {
        this.building_num = building_num;
    }

    public Double getBuilding_x() {
        return building_x;
    }

    public void setBuilding_x(Double building_x) {
        this.building_x = building_x;
    }

    public Double getBuilding_y() {
        return building_y;
    }

    public void setBuilding_y(Double building_y) {
        this.building_y = building_y;
    }

    public String getBuilding_img() {
        return building_img;
    }

    public void setBuilding_img(String building_img) {
        this.building_img = building_img;
    }
}
