# 골목모아 API 명세서

Base URL: `http://localhost:8080`

## MVP 결정사항

- 현재 결제는 Mock 결제이므로 모집 목표 미달 시 실제 환불 API는 구현하지 않습니다.
- 모집이 마감되면 `CANCELED` 또는 `FAILED` 상태로 종료하고, 참여자의 결제 상태만 취소 처리합니다.
- 실제 PG 결제 연동 시 참여자별 환불 API와 환불 완료 상태를 별도 추가합니다.

## 상점 등록과 모집 슬롯의 차이

| 구분 | 상점 등록 | 모집 슬롯 생성 |
|---|---|---|
| 목적 | 사장님의 상점 정보를 등록 | 특정 시간에 공동주문을 모집 |
| 생성 시점 | 사장님이 처음 상점을 등록할 때 | 등록된 상점에서 주문을 받을 때마다 |
| 주요 정보 | 상점명, 소개, 주소, 전화번호, 메뉴 | 모집 제목, 모집 내용, 목표 인원, 수령 장소, 시작·마감 시간 |
| 생성 횟수 | 사장님당 기본 1개 | 같은 상점에서 여러 번 생성 가능 |
| 연결 관계 | 사장님 1명과 상점 1개 | 상점 1개에 여러 모집 슬롯 |

즉, 상점 등록은 **“어떤 가게인가”**를 저장하는 기능이고, 모집 슬롯은 **“언제 몇 명을 모아 주문할 것인가”**를 정하는 기능입니다.

현재 인증 구현은 로그인 응답의 `memberId`를 상점 등록 요청의 `X-Member-Id` 헤더로 전달하는 임시 방식입니다. 학생 기능과 동일한 헤더명을 사용하며, JWT 인증 연동 시 해당 부분은 교체합니다.

## 공통 응답 형식

```json
{
  "success": true,
  "code": "COMMON200",
  "message": "요청이 성공했습니다.",
  "result": {}
}
```

## 1. 사장님 회원가입

`POST /api/members/signup/store`

### Request

```json
{
  "storeName": "골목 테스트 식당",
  "loginId": "owner01",
  "password": "password123"
}
```

### Response `200 OK`

```json
{
  "success": true,
  "code": "COMMON200",
  "message": "요청이 성공했습니다."
}
```

## 2. 학생 회원가입

`POST /api/members/signup/student`

### Request

```json
{
  "loginId": "student01",
  "password": "password123",
  "university": "연세대학교",
  "nickname": "홍길동"
}
```

### Response `200 OK`

사장님 회원가입과 동일한 공통 성공 응답을 반환합니다.

## 3. 로그인

`POST /api/members/login`

### Request

```json
{
  "loginId": "owner01",
  "password": "password123"
}
```

### Response `200 OK`

```json
{
  "success": true,
  "code": "COMMON200",
  "message": "요청이 성공했습니다.",
  "result": {
    "memberId": 1,
    "loginId": "owner01",
    "role": "STORE",
    "redirectPath": "/store"
  }
}
```

`role` 값은 사장님 `STORE`, 학생 `STUDENT`입니다.

## 4. 상점 등록

`POST /api/v1/owner/stores`

### Headers

| 이름 | 필수 | 설명 |
|---|---:|---|
| `Content-Type` | O | `application/json` |
| `X-Member-Id` | O | 로그인 응답의 `result.memberId` |

### Request

```json
{
  "name": "골목 테스트 식당",
  "category": "KOREAN",
  "shortIntroduction": "신촌 학생을 위한 한식",
  "description": "공동주문 테스트용 식당입니다.",
  "address": "서울 서대문구 신촌로 1",
  "phoneNumber": "0212345678",
  "menus": [
    {
      "name": "김치찌개",
      "price": 8000,
      "description": "대표 메뉴",
      "representative": true
    },
    {
      "name": "된장찌개",
      "price": 7500,
      "description": "구수한 메뉴",
      "representative": false
    }
  ]
}
```

### 입력 규칙

- 메뉴는 1개 이상 5개 이하
- 대표 메뉴는 정확히 1개
- `category`: `KOREAN`, `CHINESE`, `JAPANESE`, `WESTERN`, `SNACK`, `CHICKEN_PIZZA`, `CAFE_DESSERT`, `ETC`
- 전화번호는 숫자와 하이픈 조합
- 가격은 1원 이상 1,000,000원 이하

### Response `201 Created`

```json
{
  "success": true,
  "code": "COMMON201",
  "message": "리소스가 생성되었습니다.",
  "result": {
    "storeId": 1,
    "ownerId": 1,
    "name": "골목 테스트 식당",
    "category": "KOREAN",
    "shortIntroduction": "신촌 학생을 위한 한식",
    "description": "공동주문 테스트용 식당입니다.",
    "address": "서울 서대문구 신촌로 1",
    "phoneNumber": "0212345678",
    "menus": [
      {
        "menuId": 1,
        "name": "김치찌개",
        "price": 8000,
        "description": "대표 메뉴",
        "representative": true
      }
    ]
  }
}
```

## 주요 오류 코드

| HTTP | 코드 | 상황 |
|---:|---|---|
| 400 | `COMMON400` | 필수값 누락, 형식 오류 |
| 400 | `STORE4001` | 대표 메뉴가 정확히 1개가 아님 |
| 401 | `MEMBER401` | 로그인 실패 |
| 409 | `MEMBER409` | 중복 로그인 아이디 |
| 409 | `STORE4091` | 사장님이 이미 상점을 등록함 |

## 5. 모집 슬롯 생성

`POST /api/v1/owner/stores/{storeId}/slots`

### Headers

| 이름 | 필수 | 설명 |
|---|---:|---|
| `X-Member-Id` | O | 로그인 응답의 `result.memberId` |

### Request Body

```json
{
  "title": "점심 한식 공동주문",
  "content": "12시까지 함께 주문할 학생을 모집합니다.",
  "targetParticipantCount": 6,
  "discountRate": 10,
  "pickupLocation": "YONSEI_MAIN_GATE",
  "pickupAt": "2026-08-30T12:00:00"
}
```

- 목표 인원은 4~8명입니다.
- 할인율은 0~30%입니다.
- 수령 시각은 분 단위가 00 또는 30이어야 합니다.
- 모집 마감 시각은 수령 시각 30분 전으로 자동 계산됩니다.
- 상점당 `RECRUITING` 상태 슬롯은 1개만 생성할 수 있습니다.

### Response `201 Created`

```json
{
  "success": true,
  "code": "COMMON201",
  "message": "리소스가 생성되었습니다.",
  "result": {
    "slotId": 1,
    "storeId": 1,
    "title": "점심 한식 공동주문",
    "content": "12시까지 함께 주문할 학생을 모집합니다.",
    "targetParticipantCount": 6,
    "currentParticipantCount": 0,
    "discountRate": 10,
    "pickupLocation": "YONSEI_MAIN_GATE",
    "pickupAt": "2026-08-30T12:00:00",
    "deadlineAt": "2026-08-30T11:30:00",
    "status": "RECRUITING"
  }
}
```

## 6. 사장님 모집 슬롯 조회

- 목록: `GET /api/v1/owner/stores/{storeId}/slots`
- 상세: `GET /api/v1/owner/stores/{storeId}/slots/{slotId}`
- Header: `X-Member-Id` 필수

## 7. 모집 슬롯 취소

`DELETE /api/v1/owner/stores/{storeId}/slots/{slotId}`

- Header: `X-Member-Id` 필수
- `RECRUITING` 상태이고 참여자가 0명일 때만 취소할 수 있습니다.
- 취소 시 상태가 `CANCELED`로 변경됩니다.

## 8. 상점 메뉴 조회

`GET /api/v1/stores/{storeId}/menus`

학생 메뉴 선택 화면에서 사용할 상점 메뉴 목록을 반환합니다. 메뉴 선택·참여·결제 처리는 학생 파트 API에서 담당합니다.

## 모집 슬롯 오류 코드

| HTTP | 코드 | 상황 |
|---:|---|---|
| 400 | `SLOT4001` | 수령 시각이 00분 또는 30분이 아님 |
| 404 | `STORE4041` | 상점을 찾을 수 없음 |
| 404 | `SLOT4041` | 모집 슬롯을 찾을 수 없음 |
| 409 | `SLOT4091` | 이미 모집 중인 슬롯이 있음 |
| 409 | `SLOT4092` | 현재 상태에서 모집 취소 불가 |
