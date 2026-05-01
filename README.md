## 프로젝트 개요
BE-A.수강 신청 시스템

<br>

## 기술 스택
- Java 17
- Spring Boot 4.0.6
- MySQL
- JPA (Hibernate)
- Redis
- Docker / Docker Compose

<br>

## 실행 방법

### 1. 사전 요구사항

- Java 17 이상
- Docker 실행 환경 필요

### 2. 애플리케이션 실행 (로컬 실행)
```bash
docker-compose up -d
./gradlew bootRun
```


### 3. 접속 정보

- http://localhost:8080


<br>

## 요구사항 해석 및 가정
본 프로젝트는 수강 신청 시스템의 핵심 요구사항인 정원 관리, 상태 전이, 동시성 제어를 중심으로 설계하였습니다.

특히 다음과 같은 상황을 중점적으로 고려하였습니다:
- 동시에 여러 사용자가 하나의 강의에 수강 신청하는 상황
- 정원이 초과되지 않도록 보장해야 하는 문제
- 수강 신청 이후 결제/취소에 따른 상태 변화 관리
- 트래픽이 몰릴 경우를 대비한 대기열 처리



<br>

## 설계 결정과 이유
### 1. 비관적 락 대신 “조건 기반 원자적 쿼리”를 통한 동시성 제어
초기에는 데이터 정합성이 중요한 정원 관리 특성상 비관적 락을 고려하였습니다.  
하지만 비관적 락은 트랜잭션 점유 시간이 길어지고, 대기 시간이 증가하여 트래픽이 몰리는 상황에서 병목현상이 발생할 수 있다고 판단하였습니다.

이에 따라 **DB의 원자성을 활용한 조건 기반 업데이트 방식**을 선택하였습니다.

```sql
UPDATE course
SET capacity = capacity - 1
WHERE id = :courseId
AND capacity > 0;
```

<br>

### 2. 상태 패턴(State Pattern)을 활용한 상태 전이 관리
초기에는 단순 Enum 기반 분기 처리(if/else)를 고려했지만, 상태가 증가할 경우 로직이 복잡해지고 유지보수가 어려워질 수 있다고 판단하였습니다.
이에 따라 상태 패턴(State Pattern)을 적용하여 잘못된 상태 변경을 사전에 방지하였습니다.

<br>

### 3. Redis SortedSet을 활용한 대기열 및 중복 요청 제어

초기에는 사용자 중복 신청 방지를 위해 Bitmap 자료구조를 고려하였으나, 대기열 요구사항까지 포함하여 SortedSet(ZSET)을 선택하였습니다. <br>
SortedSet은 Score(요청 시간)을 기준으로 값이 자동으로 정렬되어 대기열 기능 구현 가능하며, Set의 특성으로 동일 사용자의 요청 또한 방지할 수 있었습니다.

```bash
- Key: enrollment:course:{courseId}:queue
- Value: userId
- Score: timestamp
```
<br>

결과적으로 다음과 같은 이유들로 **Redis를 필터 역할로 사용하여 DB 부하를 줄이고, 시스템의 처리량을 향상**시켰습니다.
``` bash
- 수강 신청 요청 시 매번 DB에서 중복 체크를 수행하지 않고 Redis에서 선처리 가능
- 트래픽이 몰리는 상황에서 DB로 직접 요청이 몰리는 것을 방지 (Buffer 역할)
- 실제로 수강 신청이 가능한 사용자만 DB에 접근하도록 제한하여 불필요한 쿼리 감소
```

<br>

### 4. 트랜잭션 내부에서 외부 네트워크 호출(Redis) 제거

트랜잭션 내부에서 Redis와 같은 외부 네트워크 호출이 발생할 경우 DB 커넥션을 점유한 상태로 대기하여, 대규모의 트래픽이 들어왔을 때는 커넥션 풀이 고갈될 가능성이 있습니다.
이를 방지하고자 TransactionTemplate를 통해 트랜잭션 범위를 직접 설정하여, Redis 관련 로직은 트랜잭션 외부에서 수행하였습니다.

<br>

### 5. 클린 아키텍처 적용

본 시스템은 향후 확장성과 테스트 용이성을 고려하여, 클린 아키텍처(Clean Architecture)를 기반으로 설계하였습니다. <br>
이로 인해 도메인 로직과 인프라 코드를 분리하고, 테스트 코드 작성에 용이해졌습니다. <br>
또한, 각 계층에 독립성이 보장되어 외부 기술 변경에도 유연하게 대처할 수 있습니다.

<br>

## 미구현 / 제약사항
- 강의별 수강생 목록 조회 기능 미구현

<br>

## AI 활용 범위
- 초기 설계 단계에서 알고리즘 검증 용도로 활용
- 비관적 락 vs 원자적 쿼리 선택 참고
- Redis 대기열 구조 학습

<br>

## API 목록 및 예시

### Course(강의) API

| Method | Endpoint              | 설명           | Query / Body          |
| ------ | --------------------- | -------------- | --------------------- |
| GET    | `/courses/{courseId}` | 강의 상세 조회  | -                     |
| GET    | `/courses`            | 강의 목록 조회  | `statuses` (optional) |
| POST   | `/courses`            | 강의 등록       | CourseRequest         |

### Enrollment(수강 신청) API

| Method | Endpoint                              | 설명                  | Query / Body      |
| ------ | ------------------------------------- | --------------------- | ----------------- |
| GET    | `/enrollments/me`                     | 내 수강 신청 목록 조회 | `page`, `size`    |
| POST   | `/enrollments`                        | 수강 신청             | EnrollmentRequest |
| PATCH  | `/enrollments/{enrollmentId}/confirm` | 수강 확정 (결제)      | -                 |
| PATCH  | `/enrollments/{enrollmentId}/cancel`  | 수강 취소             | -                 |


<br>
<br>

## 수강 신청 API 처리 흐름

수강 신청은 동시성 제어 및 대기열 처리를 위해 다음과 같은 순서로 동작합니다.

### 1. Redis를 통한 중복 요청 및 대기열 처리

- 사용자의 수강 신청 요청이 들어오면 Redis SortedSet에 추가됩니다.
- 동일 사용자는 하나의 course에 대해 한 번만 등록될 수 있습니다.
- 요청 시간(timestamp)을 score로 사용하여 대기열을 구성합니다.


### 2. 대기열 순서 확인

- 현재 사용자의 대기열 순서를 조회합니다.
- 정원(capacity) 이내의 사용자만 실제 수강 신청을 진행할 수 있습니다.
- 정원을 초과한 경우 대기 상태를 유지합니다.


### 3. DB 수강 신청 처리 (동시성 제어 핵심)

- 정원 이내인 경우에만 DB에 접근하여 수강 신청을 시도합니다.
- 아래와 같은 **조건 기반 원자 쿼리**를 사용하여 정원을 차감합니다.
```bash
UPDATE course  
SET capacity = capacity - 1  
WHERE id = :courseId  
AND capacity > 0;
```
- 업데이트 결과가 0건일 경우 → 정원 초과로 실패 처리


### 4. Enrollment 생성

- 정원 차감에 성공한 경우에만 Enrollment를 생성합니다.
- 상태는 `PENDING`으로 저장됩니다.


### 5. Redis 대기열 정리

- 수강 신청이 완료된 사용자는 대기열에서 제거됩니다.

### 6. EnrollmentResponse 반환
- 대기열 유무와 남은 대기열 숫자를 주입하여 반환합니다.

<br>

## 설계 의도
- Redis를 활용하여 **DB 부하를 최소화**
- 조건 기반 쿼리를 통해 **락 없이 동시성 제어**
- 대기열을 통해 **트래픽 폭주 상황에서도 안정적인 처리 보장**

<br>

## 데이터 모델 설명

### ERD

![ERD](https://github.com/user-attachments/assets/169dc77b-580b-4de0-99d9-9f06ff1e4671)

<br>

### Entity (도메인)

#### User (사용자)

시스템을 이용하는 사용자 정보를 관리합니다.

| 필드명       | 타입            | 설명     |
| ------------ | --------------- | -------- |
| id           | Long            | 고유 식별자 |
| name         | String          | 사용자 이름 |
| createdAt    | LocalDateTime   | 가입 일시  |

---

#### Course (강의)

강의 정보 및 수강 가능 인원을 관리합니다.  
정원(capacity)은 동시성 제어의 핵심 데이터로, 원자적 쿼리를 통해 관리됩니다.

| 필드명         | 타입        | 설명                          |
| -------------- | ----------- | ----------------------------- |
| id             | Long        | 고유 식별자                  |
| name           | String      | 강의명                       |
| description    | String      | 강의 상세 설명               |
| price          | Integer     | 수강료                       |
| capacity       | Integer     | 잔여 수강 가능 인원           |
| startDate      | LocalDate   | 강의 시작일                  |
| endDate        | LocalDate   | 강의 종료일                  |
| status         | Enum        | 강의 상태 (DRAFT, OPEN, CLOSED) |

---

#### Enrollment (수강 신청)

사용자와 강의 간의 신청 및 상태를 관리합니다.  
하나의 유저는 동일 강의에 대해 중복 신청이 불가능하도록 제어됩니다.

| 필드명             | 타입            | 설명                                    |
| ------------------ | --------------- | --------------------------------------- |
| id                 | Long            | 고유 식별자                            |
| user               | User            | 신청자                                 |
| course             | Course          | 신청 강의                              |
| status             | Enum            | 신청 상태 (PENDING, CONFIRMED, CANCELLED) |
| paymentDateTime    | LocalDateTime   | 결제(확정) 일시                         |

<br>

## 테스트 실행 방법
```bash
./gradlew test
```
- Testcontainers를 사용하여 MySQL, Redis 컨테이너가 자동으로 생성 및 실행됩니다.
- 별도로 docker-compose를 실행할 필요는 없습니다.
- **단, Docker 엔진은 반드시 실행 중이어야 합니다.**
