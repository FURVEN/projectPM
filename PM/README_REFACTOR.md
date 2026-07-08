# PM 그룹웨어 — 레거시 안정화 리팩토링 가이드

레거시 Spring MVC(XML) + JSP/Tiles + MyBatis + Oracle 그룹웨어를
**동작을 보존하면서** 단계적으로 안정화한 리팩토링 패스의 실행 가이드입니다.

문서 세트:

- [docs/refactor/00-current-state.md](docs/refactor/00-current-state.md) — 리팩토링 전 상태 진단
- [docs/refactor/01-dependency-review.md](docs/refactor/01-dependency-review.md) — 의존성 전수 리뷰
- [docs/refactor/02-modernization-roadmap.md](docs/refactor/02-modernization-roadmap.md) — 개선 내역과 다음 단계

## 요구 사항

| 항목 | 버전/비고 |
|---|---|
| JDK | 8 이상 (소스/타깃은 1.8. JDK 21에서도 빌드·테스트 확인됨) |
| Maven | 3.6+ |
| Oracle | 11g+ (기본 접속: `jdbc:oracle:thin:@127.0.0.1:1521:xe`). **스키마/시드 SQL 은 저장소에 없음** — 기존 운영 DB 필요 (로드맵 4단계 참조) |
| 서블릿 컨테이너 | tomcat7-maven-plugin 내장 실행 또는 외부 Tomcat 7~9 (javax 네임스페이스) |

## 로컬 실행 방법

```bash
# 1. 로컬 설정 파일 생성 (커밋되지 않음)
cp PM/src/main/resources/application-local.example.properties \
   PM/src/main/resources/application-local.properties
# → DB 계정/비밀번호, AES 키 등을 채운다

# 2. 테스트 (Oracle 불필요)
mvn -f PM/pom.xml clean test

# 3. WAR 패키징
mvn -f PM/pom.xml package -DskipTests

# 4. 내장 톰캣 실행 (Oracle 기동 상태에서)
mvn -f PM/pom.xml tomcat7:run
# → http://localhost:8080/login.pm
```

설정 파일 대신 환경변수/시스템 프로퍼티로도 주입 가능:
`PM_DATASOURCE_USERNAME`, `PM_DATASOURCE_PASSWORD`, `PM_CRYPTO_AES_KEY`,
`PM_MAIL_SMTP_USER`, `PM_MAIL_SMTP_PASSWORD` (환경변수가 파일보다 우선).

## 이번 리팩토링 요약

- **비밀정보 외부화**: DB 계정, AES256 키, Gmail 앱 비밀번호를 XML/소스에서 제거하고 로컬 프로퍼티/환경변수로 이동. ⚠️ **과거 커밋에 노출된 값들은 반드시 rotate 할 것.**
- **빌드 재현성**: Maven Central 에 없는 JAI 의존성을 JDK `ImageIO` 로 대체, OSGeo 서드파티 저장소 제거 → 어떤 환경에서도 Central 만으로 빌드.
- **로그인 모듈 정리**: 생성자 주입, 파라미터 검증, Jackson 직렬화(AJAX 응답 형태 동일 유지), 로그아웃 시 세션 무효화, 세션 키 `loginuser` 상수화(`SessionConst`).
- **의존성 정리**: 저장소 전수 검색으로 증명된 미사용 5종 제거, 죽은 빌드 플러그인 2종 제거, `spring-test` test 스코프화.
- **테스트 12개 추가**: 로그인 컨트롤러/서비스, AES256, 설정 example 파일 — 전부 Oracle/톰캣 없이 `mvn test` 로 실행.
- **저장소 위생**: 커밋되어 있던 `PM/target` 빌드 산출물 제거, `.gitignore` 정비.

기존 URL(`*.pm`), JSP/Tiles 화면, MyBatis 매퍼, DB 스키마는 변경하지 않았습니다.

## 알려진 한계

- 이 리팩토링은 Oracle 이 없는 환경에서 수행되어 **톰캣 기동 + 실제 로그인 E2E 는 미검증** (컴파일·단위테스트·WAR 패키징으로 검증). 로컬 Oracle 환경에서 첫 기동 시 로그인 플로우 확인 권장.
- `emp.checkLogin` 쿼리가 비밀번호를 DB 값과 직접 비교한다 — 저장 형태(평문 여부)에 따라 심각한 보안 리스크일 수 있으며, 해싱 전환은 데이터 마이그레이션과 함께 로드맵 4단계로 계획되어 있다.
- 로그인 외 모듈들은 아직 테스트가 없다.
