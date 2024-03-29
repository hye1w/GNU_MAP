package com.example.gnu_map;

import android.os.Parcel;
import android.os.Parcelable;

public class Bookmark implements Parcelable {
    private String buildingImg;
    private String buildingNum;
    private String buildingName;
    private String campus;
    public Bookmark() {
        // 기본 생성자
    }
    public Bookmark(String buildingImg, String buildingNum, String buildingName, String campus) {
        this.buildingImg = buildingImg;
        this.buildingNum = buildingNum;
        this.buildingName = buildingName;
        this.campus = campus;
    }

    protected Bookmark(Parcel in) {
        buildingImg = in.readString();
        buildingNum = in.readString();
        buildingName = in.readString();
        campus = in.readString();
    }

    public static final Creator<Bookmark> CREATOR = new Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel in) {
            return new Bookmark(in);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };

    public String getBuildingImg() {
        return buildingImg;
    }

    public String getBuildingNum() {
        return buildingNum;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getCampus() {
        return campus;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buildingImg);
        dest.writeString(buildingNum);
        dest.writeString(buildingName);
        dest.writeString(campus);
    }
}

