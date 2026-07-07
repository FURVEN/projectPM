package com.project.pm.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * AES256 유틸 회귀 테스트.
 * 키는 테스트 전용 더미값이다. (실제 운영 키는 절대 커밋하지 않는다)
 */
public class AES256Test {

    private static final String TEST_KEY = "unit-test-key-0123456789"; // 16자 이상

    @Test
    public void encryptThenDecryptReturnsOriginal() throws Exception {
        AES256 aes = new AES256(TEST_KEY);

        String plain = "주민등록번호-901231-1234567";
        String encrypted = aes.encrypt(plain);

        assertNotNull(encrypted);
        assertNotEquals(plain, encrypted);
        assertEquals(plain, aes.decrypt(encrypted));
    }

    @Test
    public void sameKeyProducesInteroperableInstances() throws Exception {
        // 서로 다른 인스턴스라도 같은 키면 복호화 가능해야 한다 (세션 간 데이터 호환)
        String encrypted = new AES256(TEST_KEY).encrypt("secret");
        assertEquals("secret", new AES256(TEST_KEY).decrypt(encrypted));
    }

    @Test
    public void differentKeyCannotRecoverPlaintext() throws Exception {
        String encrypted = new AES256(TEST_KEY).encrypt("secret");

        try {
            String decrypted = new AES256("another-key-9876543210ab").decrypt(encrypted);
            // 드물게 패딩이 우연히 맞아 예외 없이 끝나더라도 원문 복원은 불가능해야 한다
            assertNotEquals("secret", decrypted);
        } catch (java.security.GeneralSecurityException expected) {
            // 일반적인 경로: 잘못된 키는 BadPaddingException 으로 실패한다
        }
    }
}
