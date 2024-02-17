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

## 📽️ 개발 결과 영상
>#### 전체 화면으로 확인 시 화질 개선됩니다.

#### · 메인페이지
- 건물번호 검색
<img src="https://github.com/hye1w/GNU_map/assets/105777703/35fd0ccd-bc7c-4bc4-bd88-6eeaa481e39e.gif" width="300" height="400"/> 

- 건물명 검색
<img src="https://github.com/hye1w/GNU_map/assets/105777703/8be97a89-067e-4c91-906c-3fb8a46521d6.gif" width="300" height="400"/>

- 즐겨찾기 삭제
<img src="https://github.com/hye1w/GNU_map/assets/105777703/c24cd74e-123e-4b46-a852-a36103a39cd4.gif" width="300" height="400"/>
 
#### · 건물 상세 정보 페이지
- 즐겨찾기 추가
<img src="https://github.com/hye1w/GNU_map/assets/105777703/e83e8674-ba5a-4d63-8a07-80fa28a5ecca.gif" width="300" height="400"/>

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
