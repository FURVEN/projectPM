package com.project.pm.common;

/**
 * SMTP 발송 계정 설정을 코드 밖(시스템 프로퍼티 또는 환경변수)에서 읽어오는 헬퍼.
 *
 * 과거에는 Gmail 계정과 앱 비밀번호가 소스코드에 하드코딩되어 있었다.
 * 비밀값은 커밋 대상이 아니므로 다음 중 한 가지 방법으로 주입한다.
 *   - JVM 시스템 프로퍼티: -Dpm.mail.smtp.user=... -Dpm.mail.smtp.password=...
 *   - 환경변수: PM_MAIL_SMTP_USER / PM_MAIL_SMTP_PASSWORD
 *
 * (GoogleMail/MySMTPAuthenticator 는 스프링 빈 주입 없이 new 로 생성되는 경로가 있어
 *  플레이스홀더 주입 대신 정적 조회를 사용한다.)
 */
public final class MailConfig {

    public static final String USER_PROPERTY = "pm.mail.smtp.user";
    public static final String PASSWORD_PROPERTY = "pm.mail.smtp.password";

    private MailConfig() {
    }

    /** Gmail 사용 시 @gmail.com 을 제외한 계정 아이디 */
    public static String smtpUser() {
        return resolve(USER_PROPERTY);
    }

    /** Gmail 사용 시 계정의 "앱 비밀번호" */
    public static String smtpPassword() {
        return resolve(PASSWORD_PROPERTY);
    }

    private static String resolve(String propertyName) {
        String value = System.getProperty(propertyName);
        if (value == null || value.isEmpty()) {
            value = System.getenv(propertyName.toUpperCase().replace('.', '_'));
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(
                "메일 발송 설정 누락: 시스템 프로퍼티 '" + propertyName + "' 또는 환경변수 '"
                + propertyName.toUpperCase().replace('.', '_') + "' 를 설정해야 합니다.");
        }
        return value;
    }
}
