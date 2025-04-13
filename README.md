# RankStream Backend

본 프로젝트는 다단계 판매사의 회원 트리 및 주문 실적을 연동하고 실적과 등급에 따른 포인트를 환급하는 시스템을 만들기 위한 토이 프로젝트입니다.
계속 개발이 진행 중인 프로젝트이므로 본 문서 및 저장소의 내용은 수시로 바뀔 수 있습니다.

## Development Env

* Lang: Kotlin(Target JVM: 21)
* Framework : Spring Boot 3
* DB : MariaDB

## Project Structures

```text
rankstream-backend

|
| api
| - internal-api
| - external-api (TODO)
| auth
| config
| domain
| exception
| batch (TODO)
```

### API

API 엔드포인트 모듈. 브라우저 및 앱 또는 외부 시스템과의 통신 엔드포인트를 제공합니다.

#### Internal API

고객사 관리자를 위한 백오피스 프론트엔드 인터페이스를 제공합니다.

#### External API

고객사 시스템이 데이터를 연동할 수 있는 인터페이스를 제공합니다.

### Auth

시스템 접근을 위한 통합 인증 모듈

### Config

API 모듈에서 의존하는 설정 모듈.

### Domain

API 모듈에서 의존하는 도메인 모듈. 도메인 엔티티를 선언하고, 데이터베이스와 통신합니다.
서비스 로직을 처리합니다.

### Exception

Domain 모듈에서 의존하는 도메인 예외 모듈. 도메인과 관련된 예외를 선언하고 처리합니다.

### Batch

일괄처리 작업 모듈.
정해진 일정에 따라 고객사 회원에게 포인트를 환급합니다.

그 외에 일괄처리가 필요한 작업을 선언하고 처리합니다.
