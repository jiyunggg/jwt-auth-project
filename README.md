# 🛡️ JWT Auth 프로젝트 (백엔드 JAVA 과제)
> - Spring Security 기반 JWT 인증/인가 서비스입니다.
> - 회원가입, 로그인, 권한 관리 기능을 제공합니다.

---
## 💡 프로젝트 개요

### 목표
- **Spring Boot**로 JWT 인증/인가 로직과 API를 구현한다.
- **Junit** 기반의 테스트 코드를 작성한다.
- **Swagger** 로 API를 문서화 한다.
- 애플리케이션을 **AWS EC2**에 배포하고, 실제 환경에서 실행되도록 구성한다.

### 기간
- 2025.07.04 ~ 2025.07.06

### 기술 스택
- **Language** <div> <img src = "https://img.shields.io/badge/java21-FF6633"/> </div>

- **Backend Framework** <div> <img src = "https://img.shields.io/badge/Spring_Boot-6DB33F?&logo=spring-boot&logoColor=white"> </div>

- **Auth** <div> <img src = "https://img.shields.io/badge/Spring_Security-6DB33F?&logo=spring-security&logoColor=white"> <img src = "https://img.shields.io/badge/JWT-6a5acd?&logo=jsonwebtokens&logoColor=white"></div>

- **DB** <div> <img src = "https://img.shields.io/badge/Spring Data JPA-6DB33F?&logo=spring-data-jpa&logoColor=white"> <img src = "https://img.shields.io/badge/H2-violet?&logo=h2-database&logoColor=white"></div>

- **Docs** <div> <img src = "https://img.shields.io/badge/Swagger-00CCCF?&logo=swagger&logoColor=white"> </div>

---

## 💡 실행 방법

1️⃣ 저장소 클론
```
git clone https://github.com/jiyunggg/jwt-auth-project
cd jwt-auth-project
```

2️⃣ 빌드
```
./gradlew clean build
```

3️⃣ 테스트 실행
```
./gradlew test
```

4️⃣ 애플리케이션 실행
```
./gradlew bootRun
```

또는
```
java -jar [jar파일경로]

ex) java -jar build/libs/jwt-auth-0.0.1-SNAPSHOT.jar
```

---

## 💡 배포 주소
🔗 **AWS EC2 서버 IP**
```
http://3.39.23.19:8080
```

🔗 **Swagger UI**
```
http://3.39.23.19:8080/swagger-ui/index.html
```
---

## 💡 API 명세
> ⚠️ **테스트 시 주의사항!**
> - '관리자 권한 부여' API는 **ADMIN 권한을 가진 토큰**으로만 접근 가능합니다.
> - 애플리케이션 실행 시 기본 `관리자 계정`이 자동 생성됩니다.
>   - **username:** `admin`
>   - **password:** `admin1234`
> - 로그인 후 얻은 인증 토큰을 Swagger 우측 상단의 'Authorize'또는 '🔒' 아이콘을 통해 입력할 수 있습니다. 

| 메서드 | 경로                | 설명             |
| ------ | ------------------- | ---------------- |
| POST   | /api/signup         | 회원가입         |
| POST   | /api/login          | 로그인 (JWT 발급)|
| PATCH  | /api/admin/users/{id}/roles | 관리자 권한 부여 |

---

## 💡 프로젝트 구조
```
src
 └── main
     └── java/com/baro/jwt_auth
         ├── common          # 전역 공통 코드 (예외 처리, 공통 엔티티)
         ├── config          # 전역 설정 (Swagger, JPA Auditing 등)
         ├── security        # 인증/인가 (JWT 유틸/필터)
         └── user            # 사용자 도메인 (레이어드 아키텍처 기반)
```

---

