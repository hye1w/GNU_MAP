package com.example.gnu_map;

public class Bookmark {

        private String buildingImg;
        private String buildingNum;
        private String buildingName;

        Bookmark(String buildingImg, String buildingNum, String buildingName) {
            this.buildingImg = buildingImg;
            this.buildingNum = buildingNum;
            this.buildingName = buildingName;
        }

        String getBuildingImg() {
            return buildingImg;
        }

        String getBuildingNum() {
            return buildingNum;
        }

        String getBuildingName() {
            return buildingName;
        }
}
