# 🏫 GNU MAP
>#### GNU 교내 건물 정보 제공 애플리케이션

## 💁‍♀️ 1인 개발  
  
## 🗓 프로젝트 진행 기간
2023.11.20 ~ 2024.02.17

## 💻 주요 페이지 및 기능
**1. 메인 페이지**
- 건물명, 건물번호 검색
- 캠퍼스 선택 및 캠퍼스 지도
- 즐겨찾기 목록
  
**2. 검색 결과 목록 페이지**
- 검색 결과에 따른 건물 정보 출력

**3. 건물 상세 정보 페이지**
- 건물 위치 마커 표시
- 즐겨찾기 기능

## 📽️ 개발 결과

#### 메인페이지
- 건물 검색
<img src = "https://github.com/user-attachments/assets/b27cf0f6-6dc7-46e7-b089-6f903e6d02a6" width="300px" height="600"/>

- 캠퍼스 선택
<img src="https://github.com/user-attachments/assets/9783058d-acf6-48be-9e4d-af26e288d76b" width="300" height="600"/>
 
#### 검색 결과 페이지
<img src="https://github.com/user-attachments/assets/9676d372-ad96-4403-8394-8add1c5de82f" width="300" height="600"/>

#### 건물 상세 정보 페이지
- 건물 정보 상세보기 & 즐겨찾기 추가
<img src="https://github.com/user-attachments/assets/206d8d51-2990-400a-a761-dca241d4cd7a" width="300" height="600"/>

## 📁DataBase 구조
```java
Bookmark
- UID
  - Campus
    - BuildingName
    - BuildingNum
    - BuildingImage
    - Latitude
    - Longitude

GajwaBuilding
- BuildingName
- BuildingNum
- BuildingImage
- Latitude
- Longitude
- Campus

ChilamBuilding
- BuildingName
- BuildingNum
- BuildingImage
- Latitude
- Longitude
- Campus

ChangwonBuilding
- BuildingName
- BuildingNum
- BuildingImage
- Latitude
- Longitude
- Campus

NaedongBuilding
- BuildingName
- BuildingNum
- BuildingImage
- Latitude
- Longitude
- Campus

TongyeongBuilding
- BuildingName
- BuildingNum
- BuildingImage
- Latitude
- Longitude
- Campus

```

## 💡 추후 개선 사항
- 데이터베이스 구조 효율성 향상
- 현위치 기준 길찾기 기능
