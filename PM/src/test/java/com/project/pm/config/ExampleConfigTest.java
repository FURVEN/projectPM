package com.project.pm.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

/**
 * application-local.example.properties 회귀 테스트.
 *
 * XML 설정(root-context.xml, servlet-context.xml)이 참조하는 플레이스홀더 키가
 * example 파일에서 누락되면 신규 개발자의 로컬 셋업이 조용히 깨지므로 키 존재를 검증한다.
 * 또한 example 파일에 실제 비밀값이 커밋되는 사고를 막기 위해 CHANGE_ME 플레이스홀더를 확인한다.
 */
public class ExampleConfigTest {

    private static final String EXAMPLE_FILE = "application-local.example.properties";

    private static final String[] REQUIRED_KEYS = {
            "pm.datasource.driver-class-name",
            "pm.datasource.url",
            "pm.datasource.username",
            "pm.datasource.password",
            "pm.crypto.aes-key",
            "pm.mail.smtp.user",
            "pm.mail.smtp.password",
    };

    private static final String[] SECRET_KEYS = {
            "pm.datasource.username",
            "pm.datasource.password",
            "pm.crypto.aes-key",
            "pm.mail.smtp.user",
            "pm.mail.smtp.password",
    };

    @Test
    public void exampleFileContainsEveryRequiredKey() throws Exception {
        Properties props = loadExample();

        for (String key : REQUIRED_KEYS) {
            assertTrue("example 파일에 키 누락: " + key, props.containsKey(key));
        }
    }

    @Test
    public void secretValuesAreOnlyPlaceholders() throws Exception {
        Properties props = loadExample();

        for (String key : SECRET_KEYS) {
            String value = props.getProperty(key, "");
            assertTrue("example 파일의 " + key + " 는 CHANGE_ME 플레이스홀더여야 한다 (실제 값: 커밋 금지)",
                    value.startsWith("CHANGE_ME"));
        }
    }

    @Test
    public void datasourceUrlDefaultStaysOracleThin() throws Exception {
        // root-context.xml 의 플레이스홀더 기본값과 짝을 이루는 값 — 무심코 바꾸면 문서/기본값이 어긋난다
        Properties props = loadExample();
        String url = props.getProperty("pm.datasource.url");
        assertNotNull(url);
        assertTrue(url.startsWith("jdbc:oracle:thin:@"));
        assertFalse(url.contains(" "));
    }

    private Properties loadExample() throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream(EXAMPLE_FILE);
        assertNotNull("클래스패스에서 " + EXAMPLE_FILE + " 를 찾을 수 없음", in);
        Properties props = new Properties();
        try {
            props.load(in);
        } finally {
            in.close();
        }
        return props;
    }
}
