# 00. 현재 상태 진단 (Current State Assessment)

> 리팩토링 착수 전 시점(2026-07)의 코드베이스 상태를 기록한 문서입니다.
> 이 문서는 "무엇을, 왜 바꾸는지"의 기준점(baseline) 역할을 합니다.

## 1. 아키텍처 요약

전형적인 XML 설정 기반의 레거시 Spring MVC 모놀리식 WAR 프로젝트입니다.

| 항목 | 내용 |
|---|---|
| 언어/런타임 | Java 8 (target), Maven WAR 패키징 |
| 프레임워크 | Spring Framework 5.3.22 (XML 설정, `web.xml` 기반 부트스트랩) |
| 뷰 | JSP + Apache Tiles 3 (레이아웃), jQuery AJAX |
| 영속성 | MyBatis 3.5 + `SqlSessionTemplate`, Oracle (ojdbc8), commons-dbcp2 커넥션 풀 |
| 인증 | 세션 기반 (세션 속성 `loginuser` + `HandlerInterceptor`) |
| URL 패턴 | `*.pm` 확장자 매핑 (`DispatcherServlet`) |
| 실행 | tomcat7-maven-plugin (`mvn tomcat7:run`) |

요청 흐름:

```
브라우저 → DispatcherServlet (*.pm)
        → LoginCheckInterceptor (loginuser 세션 검사, 로그인/정적자원 제외)
        → @Controller → Service → DAO(SqlSessionTemplate) → mapper/*.xml → Oracle
        → TilesViewResolver / InternalResourceViewResolver → JSP
```

## 2. 주요 모듈 (`com.project.pm.*`)

| 패키지 | 역할 |
|---|---|
| `login` | 로그인/로그아웃/비밀번호 찾기 (controller/service/model 3계층) |
| `notice` | 공지사항 (로그인 후 랜딩 페이지) |
| `employee`, `member` | 임직원/인사 관리 |
| `commute` | 출퇴근 관리 |
| `leave` | 휴가 관리 |
| `schedule` | 일정 관리 |
| `workflow` | 전자결재 |
| `messenger` | 쪽지/메신저 |
| `alarm` | 알림 |
| `chart` | 통계 (Highcharts) |
| `admin`, `manage` | 관리자 기능 |
| `interceptor` | `LoginCheckInterceptor` (로그인 여부 검사) |
| `aop` | 컨트롤러 포인트컷 AOP |
| `common` | AES256, SHA256, 파일 업/다운로드, 엑셀, 메일 등 유틸 |
| `excel`, `file`, `exception`, `parameter`, `main` | 보조 기능 |

- MyBatis 매퍼: `src/main/resources/mapper/` — `emp`, `manage`, `notice`, `service`, `workflow` 네임스페이스 5개
- JSP: `WEB-INF/views/**` 53개 + Tiles 레이아웃 (`WEB-INF/tiles/**`)
- 정적 자원: `webapp/resources/**` (Highcharts 배포본 전체 포함)

## 3. 설정 파일

| 파일 | 역할 |
|---|---|
| `src/main/webapp/WEB-INF/web.xml` | `ContextLoaderListener`, `DispatcherServlet`(`*.pm`), 인코딩 필터 |
| `src/main/webapp/WEB-INF/spring/root-context.xml` | DataSource, `SqlSessionFactoryBean`, `SqlSessionTemplate`, 트랜잭션 매니저 |
| `src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml` | MVC 설정, Tiles/ViewResolver, 멀티파트, AES256 빈, 컴포넌트 스캔, 인터셉터 |
| `src/main/webapp/WEB-INF/tiles/tiles-layout.xml` | Tiles 레이아웃 정의 |
| `src/main/resources/log4j2.xml` | 로깅 |
| `PM/pom.xml` | 빌드/의존성 |

## 4. 알려진 리스크 (리팩토링 전)

### 4.1 커밋된 비밀정보 (High)

- `root-context.xml`: Oracle 접속 계정/비밀번호 하드코딩
- `servlet-context.xml`: AES256 암호화 키 하드코딩 (빈 생성자 인자)
- `common/SecretMyKey.java`: 동일한 AES 키가 상수로 중복 하드코딩 (참조하는 코드 없음)
- `common/MySMTPAuthenticator.java`: Gmail 계정 + 앱 비밀번호 하드코딩
- **git 히스토리에 이미 노출되었으므로, 값 자체(DB 비밀번호, AES 키, Gmail 앱 비밀번호)는 폐기/교체(rotate)가 필요합니다.** 코드에서 제거하는 것만으로는 부족합니다.

### 4.2 빌드 재현성 (High)

- `javax.media:jai_codec:1.1.3`은 Maven Central에 존재하지 않고 OSGeo 서드파티 저장소에서만 받을 수 있어, 해당 저장소 접근이 불가능한 환경에서는 `mvn` 빌드가 의존성 해석 단계에서 실패합니다 (아래 5절 참조).
- JAI(jai_core/jai_codec)는 `common/FileManager`의 `getImageWidth()`/`getImageHeight()` 두 메서드에서만 사용되며, 이 메서드들은 **호출하는 코드가 없습니다** (저장소 전체 grep 기준). JDK 표준 `ImageIO`로 대체 가능합니다.

### 4.3 저장소 위생 (Medium)

- Maven 빌드 산출물 `PM/target/**` 이 git에 커밋되어 있음 (컴파일된 `.class`, WAR 전개본 포함)
- `.gitignore`에 `target/` 및 로컬 설정 파일 패턴 부재

### 4.4 코드 품질 (Medium)

- 세션 키 `"loginuser"` 문자열이 컨트롤러 10곳 + 인터셉터에 총 65회 매직 스트링으로 반복
- 컨트롤러의 필드 주입(`@Autowired` 필드) 일관 사용, `LoginController`에는 사용하지 않는 `LoginDAO` 주입 존재
- `@ResponseBody` 응답을 `org.json`으로 수동 직렬화한 문자열 반환 (Jackson 컨버터가 등록되어 있음에도)
- 로그아웃이 `session.invalidate()` 대신 `removeAttribute("loginuser")`만 수행
- 로그인 파라미터(email/pwd) 미검증
- 비밀번호가 DB에 어떤 형태로 저장되는지 코드상 보장 없음 — `emp.checkLogin` 쿼리가 `pwd = #{pwd}` 평문 비교 (SHA-256 유틸 `Sha256.java`는 존재하나 로그인 경로에서 참조되지 않음). **이 패스에서는 DB 스키마/데이터를 알 수 없어 동작 보존을 위해 변경하지 않고 리스크로만 기록합니다.**

### 4.5 의존성 (Low~Medium)

- JSON 라이브러리 4종 공존: org.json(사용), Jackson(컨버터), Gson(chart 1곳), json-simple(**미사용**)
- `log4jdbc-remix`, `thumbnailator`, `spring-websocket`: 소스/설정 어디에서도 참조 없음 (미사용)
- `spring-test`가 compile 스코프로 선언됨 (main 코드에서 미사용)
- `maven-eclipse-plugin`(퇴역), `exec-maven-plugin`(존재하지 않는 `org.test.int1.Main` 참조) 등 죽은 빌드 플러그인
- 상세 분석: [01-dependency-review.md](01-dependency-review.md)

## 5. 빌드/실행 이슈 (baseline 검증 결과)

이 리팩토링 작업 환경: OpenJDK 21 (target 1.8 유지), Maven 3.9, **Oracle DB/외부 네트워크 제한** (프록시 정책상 `repo.osgeo.org` 접근 차단).

| 명령 | 결과 |
|---|---|
| `mvn -f PM/pom.xml clean test` | **실패 (리팩토링 이전부터 존재하던 문제)** — `javax.media:jai_codec:pom:1.1.3` 해석 불가: OSGeo 저장소 403(차단), Maven Central 404(미존재). 컴파일 단계 도달 전 의존성 해석에서 중단 |
| `mvn -f PM/pom.xml package -DskipTests` | 동일 원인으로 실패 |

- 이 실패는 본 리팩토링이 도입한 것이 아니라, 단일 서드파티 저장소에 의존하는 기존 POM 구조의 재현성 문제입니다.
- 조치: JAI 사용부(미호출 메서드 2개)를 JDK `ImageIO`로 대체하고 JAI 의존성/OSGeo 저장소를 제거 → Maven Central만으로 빌드 가능하게 함. 대체 후 빌드/테스트 결과는 로드맵 문서와 커밋 로그에 기록.
- 런타임 실행(`tomcat7:run`) 검증은 Oracle DB 없이는 로그인 플로우를 실제로 수행할 수 없어 이 환경에서는 불가능합니다. 이 한계는 정직하게 문서화하며, DB 없이 실행 가능한 단위 테스트로 회귀 범위를 커버합니다.

## 6. 리팩토링 전략 (이번 패스)

1. **동작 보존이 최우선.** URL(`*.pm`), JSP/AJAX 응답 형태, MyBatis 매퍼, DB 스키마는 변경하지 않는다.
2. 설정 외부화: DB/AES/메일 비밀정보를 XML에서 프로퍼티 플레이스홀더로 이동, 예시 파일 제공, 실제 파일은 git 제외.
3. 로그인 모듈 정리: 생성자 주입, 세션 키 상수화, 파라미터 검증, Jackson 직렬화 (응답 JSON 형태는 동일 유지).
4. 증명된 미사용 의존성만 제거, 나머지는 문서화.
5. Oracle 없이 실행 가능한 단위 테스트 추가.
6. 문서화: 현재 상태(본 문서), 의존성 리뷰, 현대화 로드맵.

## 7. 이번 패스에서 의도적으로 변경하지 않는 것

| 항목 | 이유 |
|---|---|
| Spring Boot 마이그레이션 | 별도 단계로 분리 (로드맵 참조). 테스트 안전망 없이 부트스트랩 방식 전환은 회귀 위험이 큼 |
| Spring Security 도입 | 인터셉터 기반 인증과의 대체는 큰 변경 — 로드맵에 문서화만 |
| 비밀번호 해싱(로그인 쿼리) 변경 | DB 데이터 마이그레이션 필요 — 스키마/데이터 변경 금지 제약에 따라 보류 |
| `*.pm` URL 패턴 | 하위 호환 유지 |
| JSP/Tiles 페이지 구조 | 뷰 레이어 재작성은 범위 밖 |
| 패키지 전면 개편 | diff 노이즈 대비 이득 없음 |
| org.json → Jackson 전면 통일 | 10개 컨트롤러 응답 형태 회귀 위험 — 로그인 모듈만 우선 적용, 나머지는 로드맵 |
| Oracle 스키마/시드 데이터 | 저장소에 스키마 덤프가 없어 검증 불가 — 로드맵에 과제로 기록 |
