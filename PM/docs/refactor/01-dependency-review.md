# 01. 의존성 리뷰 (Dependency Review)

> `PM/pom.xml` 전수 검토 결과. "사용 여부"는 저장소 전체 grep(자바 소스, JSP, 스프링 XML 설정)으로 검증했다.
> 원칙: **증명된 미사용만 제거**하고, 위험한 정리는 문서화만 한다.

## 1. 이번 패스에서 제거한 것

| 의존성 | 근거 |
|---|---|
| `javax.media:jai_core`, `jai_codec` 1.1.3 | `FileManager.getImageWidth/Height` 두 메서드에서만 사용(호출부 없음). `jai_codec`은 Maven Central에 없어 OSGeo 저장소 없이는 빌드가 깨짐. JDK `ImageIO`로 대체 후 제거 |
| OSGeo `<repository>` | JAI 전용이었음. 제거로 Maven Central 단일 저장소 빌드 확보 (재현성 개선) |
| `com.googlecode.json-simple:json-simple` | `org.json.simple`, `JSONParser`, `JSONValue` 참조 0건 |
| `org.lazyluke:log4jdbc-remix` | 소스/설정 어디에도 `log4jdbc` 참조 없음 (datasource 는 Oracle 드라이버 직접 사용) |
| `net.coobird:thumbnailator` | `Thumbnails`/`coobird` 참조 0건 |
| `org.springframework:spring-websocket` | WebSocket 관련 자바 코드 0건. `web.xml`의 websocketContext 는 주석 상태 |
| `maven-eclipse-plugin` (플러그인) | 퇴역한 Eclipse IDE 통합 플러그인. 빌드 라이프사이클에 미관여 |
| `exec-maven-plugin` (플러그인) | 존재하지 않는 `org.test.int1.Main`을 가리키는 아키타입 잔재 |

## 2. 스코프/구조 수정

| 항목 | 변경 |
|---|---|
| `spring-test` | compile → **test** 스코프 (main 소스에서 `org.springframework.test`/`mock` 참조 없음, WAR 용량 감소) |

## 3. 유지한 것 (겉보기에 불필요해 보여도 실제 필요)

| 의존성 | 이유 |
|---|---|
| `commons-fileupload` | 자바 코드에서 직접 import 하지 않지만 `servlet-context.xml`의 `CommonsMultipartResolver`가 **런타임에 요구** |
| `javax.mail` + `activation` | `GoogleMail`/`MySMTPAuthenticator`가 사용 (현재 호출부는 없으나 기능 코드로 보존) |
| `cglib` | `<aop:config proxy-target-class="true">` 클래스 프록시. (Spring 5는 내장 cglib을 사용하므로 사실상 불필요할 가능성이 높지만, 런타임 검증 없이 제거하지 않음 — 차기 과제) |
| `org.json` | 컨트롤러 8곳에서 수동 JSON 직렬화에 사용 중 |
| Gson | `ChartController` 1곳 사용 |
| Jackson (databind/core) | `MappingJackson2HttpMessageConverter` + `@ResponseBody` 직렬화 |
| Apache POI (poi, poi-ooxml) | 엑셀 업로드/다운로드 (`common/Excel*`, `excel/ExcelDownloadView`) |
| lombok | VO 클래스 전반에서 사용 |
| log4j2 + slf4j 브리지 | 로깅 스택 |

## 4. JSON 라이브러리 4종 현황과 통합 방향

| 라이브러리 | 상태 | 방향 |
|---|---|---|
| json-simple | 미사용 → **제거됨** | — |
| org.json | 컨트롤러 8곳에서 응답 문자열 수동 조립 | Jackson `@ResponseBody` 직렬화로 점진 통합 (로그인 모듈은 이번 패스에서 완료). 응답 형태 회귀 위험 때문에 모듈 단위로 테스트와 함께 진행 |
| Gson | ChartController 1곳 | 동일하게 Jackson으로 통합 후 제거 가능 |
| Jackson | MVC 메시지 컨버터 | **표준으로 유지** |

## 5. 이번 패스에서 하지 않은 것 (위험/보류 항목)

| 항목 | 이유 |
|---|---|
| Spring 5.3.22 → 5.3.39 패치 업그레이드 | 안전할 가능성이 높고 CVE 픽스 포함 — 다만 이 환경에서 런타임 검증(톰캣 기동, 로그인 플로우)이 불가능해 보류. **차기 패스 1순위 추천** |
| commons-dbcp2 → HikariCP | 성능/안정성 개선이지만 런타임 검증 필요 |
| `oracle.jdbc.driver.OracleDriver` → `oracle.jdbc.OracleDriver` | deprecated 클래스명이지만 동작 변화 없음. 설정 기본값은 유지하고 example 파일에 신형 클래스명 기재 |
| tomcat7-maven-plugin 대체 | 퇴역 플러그인(서블릿 3.0 톰캣 7 내장). cargo/jetty 등 대체는 실행 방식 변경이라 별도 검증 필요 |
| javax → jakarta 네임스페이스 | 톰캣 10+/Spring 6 전환과 묶이는 대형 변경 — 로드맵 참조 |
| JUnit 4 → 5 | 신규 테스트가 JUnit 4로 작성됨(기존 스택 유지). 테스트 자산이 커지기 전 어느 시점에 전환 권장 |
