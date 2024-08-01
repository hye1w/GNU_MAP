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
<img src = "https://github.com/hye1w/IMAGES/blob/main/GNU%20MAP/%EB%A9%94%EC%9D%B8.jpg?raw=true" width="300px" height="600"/>

- 캠퍼스 선택
<img src="https://github.com/hye1w/IMAGES/blob/main/GNU%20MAP/%EB%A9%94%EC%9D%B8%20%EC%85%80%EB%A0%89%ED%8A%B8%EB%B0%95%EC%8A%A4.jpg?raw=true" width="300" height="600"/>
 
#### 검색 결과 페이지
<img src="https://github.com/hye1w/IMAGES/blob/main/GNU%20MAP/%EA%B2%B0%EA%B3%BC.jpg?raw=true" width="300" height="600"/>

#### 건물 상세 정보 페이지
- 건물 정보 상세보기 & 즐겨찾기 추가
<img src="https://github.com/hye1w/IMAGES/blob/main/GNU%20MAP/%EC%A6%90%EA%B2%A8%EC%B0%BE%EA%B8%B0.jpg?raw=true" width="300" height="600"/>

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
