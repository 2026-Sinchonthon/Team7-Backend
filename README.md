# Team7 Backend

## 서비스 설명
- **프로젝트명**: 모아모아
- **슬로건**: 골목이 모이면, 배달이 열린다
- **한 줄 소개**: 신촌 골목 안 개인 상점(프랜차이즈 제외) 전용 — 관심이 모이면 배달이 열리는 발견형 공동주문 서비스
- **주요 이용자**
    - 연세대·이화여대·서강대 신촌 생활권 대학생
    - 신촌 골목 안 개인 상점 (프랜차이즈·본사 마케팅 지원 매장 제외)

## 팀원 소개
| 역할     | 이름  | 소속      |
| ------ | --- | ------- |
| 기획·디자인 | 이윤서 | 연세대학교   |
| 기획·디자인 | 정우성 | 서강대학교   |
| 프론트엔드  | 김가영 | 이화여자대학교 |
| 백엔드    | 박서준 | 명지대학교   |
| 백엔드    | 박채원 | 이화여자대학교 |
| 백엔드    | 이한재 | 홍익대학교   |

## 기술 스택

| 구분 | 기술 |
|---|---|
| 기획/디자인 | Figma |
| 프론트엔드 | React |
| 백엔드 | Spring Boot, Spring Data JPA, Spring Security, Validation |
| 데이터베이스 | MySQL |
| API 문서 | Swagger |
| 배포 | Render, Docker |
| 버전 관리 | Git, GitHub |
| 문서 관리 | Notion |

## 프로젝트 구조

```text
src/main/java/sinchonthon/demo
├── domain
│   ├── member
│   ├── discovery
│   └── store
└── global
    ├── config
    ├── exception
    ├── health
    └── response
```

## 실행 방법

### 로컬 실행

```bash
./gradlew bootRun
```

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

### 테스트

```bash
./gradlew test
```

Windows PowerShell:

```powershell
.\gradlew.bat test
```

## 환경 변수

| 이름 | 설명 | 예시 |
|---|---|---|
| `DB_URL` | MySQL JDBC URL | `jdbc:mysql://HOST:PORT/railway?serverTimezone=Asia/Seoul&characterEncoding=UTF-8` |
| `DB_USERNAME` | MySQL 사용자명 | `root` |
| `DB_PASSWORD` | MySQL 비밀번호 | `password` |
| `SERVER_PORT` | 로컬 서버 포트 | `8080` |
| `PORT` | Render에서 자동으로 주입하는 포트 | Render 자동 설정 |

## 배포

Render Docker 배포를 기준으로 합니다.

| 항목 | 값 |
|---|---|
| Runtime | Docker |
| Branch | `main` |
| Dockerfile | `Dockerfile` |
| Health Check Path | `/health` |

Render 환경 변수에는 아래 값을 설정합니다.

```env
DB_URL=jdbc:mysql://HOST:PORT/railway?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=root
DB_PASSWORD=MYSQLPASSWORD
```

## API 공통 응답

### 성공 응답

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "요청이 성공했습니다.",
  "result": {}
}
```

### 실패 응답

```json
{
  "isSuccess": false,
  "code": "COMMON400",
  "message": "잘못된 요청입니다."
}
```

## API 명세

### Health

| 구분 | Method | URL | 설명 |
|---|---|---|---|
| 루트 확인 | GET | `/` | 서버 실행 상태를 확인합니다. |
| 헬스 체크 | GET | `/health` | 배포 서버 상태를 확인합니다. |

### Member

| 구분 | Method | URL | 설명 |
|---|---|---|---|
| 가게 회원가입 | POST | `/api/members/signup/store` | 사장/가게 회원을 생성합니다. |
| 대학생 회원가입 | POST | `/api/members/signup/student` | 대학생 회원을 생성합니다. |
| 로그인 | POST | `/api/members/login` | 아이디와 비밀번호로 로그인하고 회원 역할에 따른 이동 경로를 반환합니다. |

#### 가게 회원가입 Request

```json
{
  "storeName": "가게 이름",
  "loginId": "아이디",
  "password": "비밀번호",
  "phoneNumber": "010-1234-5678"
}
```

#### 대학생 회원가입 Request

```json
{
  "university": "연세대학교",
  "nickname": "닉네임",
  "loginId": "아이디",
  "password": "비밀번호",
  "phoneNumber": "010-1234-5678"
}
```

#### 로그인 Request

```json
{
  "loginId": "아이디",
  "password": "비밀번호"
}
```

#### 로그인 Response

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "요청이 성공했습니다.",
  "result": {
    "memberId": 1,
    "loginId": "student123",
    "role": "STUDENT",
    "redirectPath": "/student",
    "storeId": null
  }
}
```

사장 회원이 가게를 등록한 뒤 로그인하면 `storeId`가 함께 반환됩니다. 아직 가게를 등록하지 않은 경우 `storeId`는 `null`입니다.

#### Member 필드

| 필드명 | 타입 | 필수 여부 | 설명 |
|---|---|---|---|
| `storeName` | String | 필수 | 가게 회원가입에서 사용하는 가게 이름입니다. |
| `university` | String | 필수 | 대학생 회원가입에서 사용하는 대학교 이름입니다. |
| `nickname` | String | 필수 | 대학생 회원가입에서 사용하는 닉네임입니다. |
| `loginId` | String | 필수 | 로그인에 사용할 아이디입니다. 중복될 수 없습니다. |
| `password` | String | 필수 | 로그인에 사용할 비밀번호입니다. 서버에서 암호화되어 저장됩니다. |
| `phoneNumber` | String | 필수 | 전화번호입니다. 숫자와 하이픈만 사용할 수 있습니다. |

#### 대학생 회원가입 허용 대학교

| 대학교 |
|---|
| 연세대학교 |
| 이화여자대학교 |
| 서강대학교 |
| 홍익대학교 |
| 명지대학교 |

#### 로그인 역할별 이동 경로

| role | redirectPath | 화면 |
|---|---|---|
| `STORE` | `/store` | 사장 화면 |
| `STUDENT` | `/student` | 대학생 화면 |

### Discovery

| 구분 | Method | URL | Header | 설명 |
|---|---|---|---|---|
| 모집 목록 조회 | GET | `/api/v1/recruitments` | 없음 | 모집 중인 가게 목록을 조회합니다. |
| 모집 상세 조회 | GET | `/api/v1/recruitments/{id}` | 없음 | 모집 상세, 메뉴, 참여 그룹, 픽업 장소를 조회합니다. |
| 주문 초안 생성 | POST | `/api/v1/recruitments/{id}/draft-orders` | `X-Member-Id` | 대학생 회원의 주문 초안을 생성합니다. |
| 주문 옵션 선택 | PATCH | `/api/v1/draft-orders/{id}/selection` | `X-Member-Id` | 주문 초안의 참여 그룹과 픽업 장소를 선택합니다. |

#### 주문 초안 생성 Request

```json
{
  "items": [
    {
      "menuId": 1,
      "quantity": 2
    }
  ]
}
```

#### 주문 옵션 선택 Request

```json
{
  "participantGroupId": 1,
  "pickupLocationId": 1
}
```

### Store

| 구분 | Method | URL | Header | 설명 |
|---|---|---|---|---|
| 가게 등록 | POST | `/api/v1/owner/stores` | `X-Member-Id` | 사장 회원의 가게와 메뉴를 등록합니다. |
| 가게 메뉴 조회 | GET | `/api/v1/stores/{storeId}/menus` | 없음 | 가게 메뉴 목록을 조회합니다. |

#### 가게 등록 Request

```json
{
  "name": "신촌분식",
  "category": "KOREAN",
  "shortIntroduction": "든든한 한식 도시락",
  "description": "신촌에서 운영하는 한식 매장입니다.",
  "address": "서울시 서대문구",
  "phoneNumber": "02-123-4567",
  "menus": [
    {
      "name": "제육덮밥",
      "price": 9000,
      "description": "매콤한 제육덮밥",
      "representative": true
    }
  ]
}
```

#### 가게 카테고리

| 값 |
|---|
| `KOREAN` |
| `CHINESE` |
| `JAPANESE` |
| `WESTERN` |
| `SNACK` |
| `CHICKEN_PIZZA` |
| `CAFE_DESSERT` |
| `ETC` |

### Recruitment Slot

| 구분 | Method | URL | Header | 설명 |
|---|---|---|---|---|
| 모집 슬롯 생성 | POST | `/api/v1/owner/stores/{storeId}/slots` | `X-Member-Id` | 사장 회원이 모집 슬롯을 생성합니다. |
| 모집 슬롯 목록 조회 | GET | `/api/v1/owner/stores/{storeId}/slots` | `X-Member-Id` | 사장 회원의 가게 모집 슬롯 목록을 조회합니다. |
| 모집 슬롯 상세 조회 | GET | `/api/v1/owner/stores/{storeId}/slots/{slotId}` | `X-Member-Id` | 모집 슬롯 상세를 조회합니다. |
| 모집 슬롯 취소 | DELETE | `/api/v1/owner/stores/{storeId}/slots/{slotId}` | `X-Member-Id` | 모집 슬롯을 취소합니다. |

#### 모집 슬롯 생성 Request

```json
{
  "title": "오늘 저녁 공동 주문",
  "content": "18시까지 모집합니다.",
  "targetParticipantCount": 4,
  "discountRate": 10,
  "pickupLocation": "YONSEI_MAIN_GATE",
  "pickupAt": "2026-08-29T18:30:00"
}
```

#### 픽업 장소

| 값 |
|---|
| `YONSEI_MAIN_GATE` |
| `EWHA_MAIN_GATE` |
| `SOGANG_MAIN_GATE` |
| `MYONGJI_MAIN_GATE` |
| `HONGIK_MAIN_GATE` |

## 인증 방식

현재 프로젝트는 JWT가 아니라 임시 헤더 기반 회원 식별 방식을 사용합니다.

로그인 성공 후 받은 `memberId`를 아래 헤더로 전달합니다.

```http
X-Member-Id: 1
```

## Swagger

배포 후 Swagger UI에서 API를 확인할 수 있습니다.

```text
https://배포주소/swagger-ui.html
```

## 참고

- 실제 DB 접속 정보가 들어가는 `.env` 파일은 커밋하지 않습니다.
- `.env.example`은 예시 파일이므로 커밋할 수 있습니다.
- 배포 DB는 Render에서 접근 가능한 MySQL을 사용합니다.
