# 학생 화면 1차 API 명세 — 가게 탐색 · 메뉴 선택 · 주문 확인

## 구현 범위

이번 범위는 아래 화면 전환까지만 구현한다.

```
가게 목록(메인) → 가게 상세(소개·메뉴 선택) → 주문 확인 페이지(참여 집단·수령 장소 선택)
```

- 크레딧, 실제 결제, 공동주문 참여 확정, 주문 내역 및 수령 처리는 이번 범위에서 제외한다.
- 주문 확인 페이지에서 생성되는 값은 `DRAFT`(임시 주문)이다. 참여 인원이나 모집글 상태에는 영향을 주지 않는다.
- 화면의 “주문 번호”에는 실제 주문 번호가 아니라 `draftOrderNumber`를 표시한다. 실제 주문 확정 기능이 생기면 그때 실제 주문 번호를 발급한다.

## 공통

- Base URL: `/api/v1`
- 인증: 로그인 API 연동 후 `Authorization: Bearer {accessToken}`을 사용한다.
- 현재 로그인 토큰 기능이 아직 없으므로, 로컬 개발에서는 `X-Member-Id: {학생 Member ID}` 헤더를 사용한다. `STORE` 회원은 임시 주문을 만들 수 없다.
- 응답 형식

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "성공입니다.",
  "result": {}
}
```

- 모든 금액은 원 단위 정수다.

## 용어 및 상태

| 용어 | 의미 |
| --- | --- |
| 모집글 (`recruitment`) | 가게가 올린 공동주문 모집 단위. 메인 화면의 카드 하나다. |
| 참여 집단 (`participant group`) | 예: 홍대, 서강대, 연세대. 학생이 주문 확인 화면에서 선택한다. |
| 수령 장소 (`pickup location`) | 예: 정문, 후문. 모집글마다 선택 가능한 장소가 다를 수 있다. |
| 임시 주문 (`draft order`) | 메뉴·집단·수령 장소 선택을 보관하는 주문 확인용 데이터. 결제/참여 확정이 아니다. |

`draftOrder.status`는 이번 범위에서 `DRAFT`만 사용한다.

---

## 1. 메인 — 모집글/가게 목록 조회

`GET /recruitments?page=0&size=20&keyword=국수`

| Query | 필수 | 설명 |
| --- | --- | --- |
| `page` | 아니오 | 0부터 시작, 기본값 0 |
| `size` | 아니오 | 기본값 20 |
| `keyword` | 아니오 | 가게명 검색어 |

### 응답 `200 OK`

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "성공입니다.",
  "result": {
    "content": [
      {
        "recruitmentId": 12,
        "restaurantId": 3,
        "restaurantName": "골목국수",
        "thumbnailUrl": "https://example.com/golmok-noodle.jpg",
        "shortDescription": "신촌 골목 20년 국수집",
        "participantCount": 2,
        "targetParticipantCount": 4,
        "orderDeadlineAt": "2026-08-29T17:30:00"
      }
    ],
    "page": 0,
    "size": 20,
    "hasNext": false
  }
}
```

프론트는 카드 선택 시 `recruitmentId`로 가게 상세를 조회한다.

---

## 2. 가게 상세 — 소개·메뉴·선택지 조회

`GET /recruitments/{recruitmentId}`

가게 상세 화면 최초 진입 시 호출한다. 메뉴 선택 UI와 다음 화면에서 사용할 참여 집단/수령 장소 선택지도 한 번에 내려준다.

### 응답 `200 OK`

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "성공입니다.",
  "result": {
    "recruitmentId": 12,
    "restaurant": {
      "restaurantId": 3,
      "name": "골목국수",
      "description": "신촌 골목에서 20년째 운영 중인 국수집입니다.",
      "imageUrl": "https://example.com/golmok-noodle.jpg"
    },
    "menus": [
      {
        "menuId": 101,
        "name": "잔치국수",
        "price": 7000,
        "imageUrl": null,
        "isAvailable": true
      },
      {
        "menuId": 102,
        "name": "비빔국수",
        "price": 8000,
        "imageUrl": null,
        "isAvailable": true
      }
    ],
    "participantGroups": [
      { "groupId": 1, "name": "홍익대학교" },
      { "groupId": 2, "name": "서강대학교" }
    ],
    "pickupLocations": [
      { "pickupLocationId": 1, "name": "홍대 정문" },
      { "pickupLocationId": 2, "name": "홍대 후문" }
    ]
  }
}
```

### 프론트 동작

학생은 `isAvailable=true` 메뉴의 수량을 선택한다. “결제하러 가기”를 누르기 전까지는 선택값을 프론트 상태에만 보관한다.

---

## 3. 주문 확인 페이지 진입 — 임시 주문 생성

가게 상세에서 “결제하러 가기”를 누를 때 호출한다. 서버는 메뉴 가격과 유효성을 다시 확인해 임시 주문을 만들고, 주문 확인 페이지가 표시할 임시 주문 번호를 반환한다.

`POST /recruitments/{recruitmentId}/draft-orders`

### 요청

```json
{
  "items": [
    { "menuId": 101, "quantity": 1 },
    { "menuId": 102, "quantity": 2 }
  ]
}
```

### 요청 검증

- `items`는 한 개 이상이어야 한다.
- `quantity`는 1 이상이어야 한다.
- 메뉴는 해당 모집글의 가게 메뉴여야 하며 판매 가능 상태여야 한다.
- 총 금액은 클라이언트 값이 아닌 서버의 최신 메뉴 가격으로 계산한다.

### 응답 `201 Created`

```json
{
  "isSuccess": true,
  "code": "COMMON201",
  "message": "주문 확인 정보를 생성했습니다.",
  "result": {
    "draftOrderId": 91,
    "draftOrderNumber": "DRAFT-20260829-0091",
    "status": "DRAFT",
    "restaurantName": "골목국수",
    "items": [
      { "menuId": 101, "menuName": "잔치국수", "unitPrice": 7000, "quantity": 1, "lineAmount": 7000 },
      { "menuId": 102, "menuName": "비빔국수", "unitPrice": 8000, "quantity": 2, "lineAmount": 16000 }
    ],
    "totalAmount": 23000,
    "participantGroup": null,
    "pickupLocation": null
  }
}
```

오류: 메뉴가 없거나 판매 불가한 경우 `400 COMMON400`, 모집글이 없으면 `404 COMMON404`.

---

## 4. 주문 확인 페이지 — 참여 집단·수령 장소 선택 저장

사용자가 참여 집단과 수령 장소를 선택하면 호출한다. 두 값을 한 번에 저장하는 방식으로 UI 구현을 단순화한다.

`PATCH /draft-orders/{draftOrderId}/selection`

### 요청

```json
{
  "participantGroupId": 2,
  "pickupLocationId": 1
}
```

### 검증

- 로그인한 학생이 만든 `DRAFT` 주문만 수정할 수 있다.
- 선택한 집단과 수령 장소가 해당 모집글에서 제공한 선택지인지 검증한다.

### 응답 `200 OK`

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "참여 집단과 수령 장소를 선택했습니다.",
  "result": {
    "draftOrderId": 91,
    "draftOrderNumber": "DRAFT-20260829-0091",
    "status": "DRAFT",
    "participantGroup": { "groupId": 2, "name": "서강대학교" },
    "pickupLocation": { "pickupLocationId": 1, "name": "홍대 정문" }
  }
}
```

오류: 다른 사용자의 임시 주문 또는 없는 임시 주문은 `404 COMMON404`, 선택지가 해당 모집글에 없으면 `400 COMMON400`.

---

## 이번 범위에서 만들 데이터

| 데이터 | 최소 필드 |
| --- | --- |
| Restaurant | id, name, description, imageUrl |
| Menu | id, restaurantId, name, price, isAvailable |
| Recruitment | id, restaurantId, targetParticipantCount, orderDeadlineAt |
| ParticipantGroup | id, recruitmentId, name |
| PickupLocation | id, recruitmentId, name |
| DraftOrder | id, studentId, recruitmentId, draftOrderNumber, status, totalAmount, participantGroupId, pickupLocationId |
| DraftOrderItem | id, draftOrderId, menuId, menuName, unitPrice, quantity |

`DraftOrderItem`에는 메뉴명과 단가를 복사해 저장한다. 이후 메뉴가 수정돼도 주문 확인 화면의 금액이 바뀌지 않는다.

## 다음 단계에서 추가할 API (이번 구현 제외)

- `POST /draft-orders/{draftOrderId}/confirm` — 임시 주문을 실제 공동주문 참여로 확정
- `GET /me/orders` — 실제 주문 내역 조회
- 모집글 목표 인원 도달/미달 처리, 수령 번호 발급
