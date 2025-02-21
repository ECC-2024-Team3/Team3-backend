# 오이마켓 - 중고거래 웹사이트

## 📌 프로젝트 소개
오이마켓은 **학교 내 중고거래 기능**을 제공하는 웹사이트입니다. 
실시간 채팅 없이 **댓글을 통한 거래 방식**을 지원하며, 안전하고 간편한 거래 환경을 목표로 합니다.

## 🚀 기술 스택
### **백엔드**
- **프레임워크**: Spring Boot
- **데이터베이스**: MySQL
- **ORM**: JPA (Hibernate)
- **빌드 툴**: Maven
- **보안**: Spring Security (현재 모든 요청 허용 상태)

## 📁 프로젝트 구조
```
ECC-2024-Team3/
│── Team3-backend/ (Spring Boot 백엔드)
│   ├── src/main/java/io/github/ecc2024team3/oimarket
│   │   ├── config/ (보안 및 설정)
│   │   ├── controller/ (API 엔드포인트)
│   │   ├── dto/ (데이터 전송 객체)
│   │   ├── entity/ (JPA 엔티티)
│   │   ├── repository/ (JPA 리포지토리)
│   │   ├── service/ (비즈니스 로직)
│   │   ├── exception/ (예외 처리)
│   │   ├── OiMarketApplication.java (메인 클래스)
│   ├── src/test/java/io/github/ecc2024team3/oimarket (테스트 코드)
```

## 🔥 주요 기능
- **게시판 CRUD**
- **좋아요 및 북마크 기능**
- **댓글을 통한 거래 및 소통**

## 🛠 개발 환경 설정
### **1. 백엔드 실행 방법**
```bash
# 프로젝트 클론
git clone https://github.com/ECC-2024-Team3/Team3-backend.git
cd oimarket/backend

# MySQL 실행 및 설정
mysql -u root -p  # MySQL 로그인 후 데이터베이스 생성
CREATE DATABASE oimarket;

# application.properties 수정
spring.datasource.url=jdbc:mysql://localhost:3306/oimarket
spring.datasource.username=root
spring.datasource.password=yourpassword

# 빌드 및 실행
mvn clean package
java -jar target/oimarket-0.0.1-SNAPSHOT.jar
```

### **2. 프론트엔드 실행 방법 (추후 업데이트 예정)**
```bash
cd frontend
npm install
npm start
```

## 📜 API 명세서 (작성 중)
- https://www.notion.so/8802952071a8459b97368a527f4a6623?v=8f1e362488894d3187a4b561868d3d7e

## ⚠️ 문제 해결
### **1. Maven 빌드 오류**
- `target/` 폴더 삭제 후 다시 빌드:
```bash
rm -rf target/
mvn clean package
```
- `spring.jpa.open-in-view=false` 설정 추가 시 LazyInitializationException 방지 가능

## 💡 팀 정보
- **Team 3** - ECC 2024
- **GitHub 그룹 ID**: `io.github.ecc2024team3`
