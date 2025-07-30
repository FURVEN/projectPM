package com.project.pm.common;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES256 양방향 암호화 알고리즘을 지원하는 유틸리티 클래스
 * 
 * 이 클래스는 보안이 필요한 데이터(비밀번호, 개인정보 등)를 암호화/복호화하는 기능을 제공합니다.
 * AES256 알고리즘과 CBC 모드, PKCS5Padding을 사용하여 안전한 암호화를 수행합니다.
 * 
 * 주요 특징:
 * - 양방향 암호화: 암호화된 데이터를 원본으로 복원 가능
 * - AES256 알고리즘 사용: 고급 암호화 표준
 * - Base64 인코딩: 암호화된 바이너리 데이터를 텍스트로 변환
 * 
 * 주의사항:
 * - 기본생성자가 없으므로 @Component 사용 불가
 * - servlet-context.xml에서 파라미터가 있는 생성자로 bean 등록 필요
 * - 키값은 16자리 이상이어야 함
 */
public class AES256 {
    
    /**
     * 초기화 벡터(Initialization Vector)
     * CBC 모드에서 첫 번째 블록 암호화 시 사용되는 랜덤 값
     */
    private String iv;
    
    /**
     * 암호화/복호화에 사용할 비밀키 스펙
     * AES 알고리즘용 SecretKeySpec 객체
     */
    private Key keySpec;

    /**
     * AES256 암호화 객체 생성자
     * 
     * 16자리의 키값을 입력받아 암호화/복호화 준비를 완료합니다.
     * 
     * 처리 과정:
     * 1. 입력된 키에서 처음 16자리를 초기화 벡터(IV)로 설정
     * 2. 키를 UTF-8 바이트 배열로 변환
     * 3. 16바이트 키 배열 생성 (AES 알고리즘 요구사항)
     * 4. SecretKeySpec 객체 생성하여 keySpec에 저장
     * 
     * @param key 암호화/복호화를 위한 키값 (16자리 이상 권장)
     * @throws UnsupportedEncodingException UTF-8 인코딩 지원하지 않을 경우 발생
     */
    public AES256(String key) throws UnsupportedEncodingException {
        // 키의 처음 16자리를 초기화 벡터로 사용
        this.iv = key.substring(0, 16);
        
        // AES 알고리즘에 필요한 16바이트 키 배열 생성
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        
        // 키 길이가 16바이트를 초과하면 16바이트로 제한
        if(len > keyBytes.length){
            len = keyBytes.length;
        }
        
        // 키 바이트를 배열에 복사
        System.arraycopy(b, 0, keyBytes, 0, len);
        
        // AES용 비밀키 스펙 생성
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        this.keySpec = keySpec;
    }

    /**
     * 문자열을 AES256으로 암호화
     * 
     * 입력된 평문 문자열을 AES256 알고리즘으로 암호화하고,
     * Base64로 인코딩하여 텍스트 형태로 반환합니다.
     * 
     * 암호화 과정:
     * 1. AES/CBC/PKCS5Padding 암호화 모드 설정
     * 2. 암호화 모드로 Cipher 객체 초기화 (키, IV 사용)
     * 3. 입력 문자열을 UTF-8 바이트로 변환
     * 4. 암호화 수행
     * 5. 결과를 Base64로 인코딩하여 반환
     * 
     * @param str 암호화할 평문 문자열
     * @return Base64로 인코딩된 암호화 문자열
     * @throws NoSuchAlgorithmException 지원하지 않는 알고리즘인 경우
     * @throws GeneralSecurityException 보안 관련 예외 발생 시
     * @throws UnsupportedEncodingException UTF-8 인코딩 지원하지 않을 경우
     */
    public String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException{
        // AES/CBC/PKCS5Padding 모드의 Cipher 객체 생성
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        // 암호화 모드로 초기화 (키와 초기화 벡터 사용)
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        
        // 문자열을 UTF-8 바이트로 변환 후 암호화 수행
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        
        // 암호화된 바이트를 Base64 문자열로 인코딩
        String enStr = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }

    /**
     * AES256으로 암호화된 문자열을 복호화
     * 
     * Base64로 인코딩된 암호화 문자열을 원본 평문으로 복원합니다.
     * 
     * 복호화 과정:
     * 1. AES/CBC/PKCS5Padding 복호화 모드 설정
     * 2. 복호화 모드로 Cipher 객체 초기화 (키, IV 사용)
     * 3. Base64 문자열을 바이트 배열로 디코딩
     * 4. 복호화 수행
     * 5. 결과를 UTF-8 문자열로 변환하여 반환
     * 
     * @param str Base64로 인코딩된 암호화 문자열
     * @return 복호화된 평문 문자열
     * @throws NoSuchAlgorithmException 지원하지 않는 알고리즘인 경우
     * @throws GeneralSecurityException 보안 관련 예외 발생 시
     * @throws UnsupportedEncodingException UTF-8 인코딩 지원하지 않을 경우
     */
    public String decrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        // AES/CBC/PKCS5Padding 모드의 Cipher 객체 생성
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        // 복호화 모드로 초기화 (키와 초기화 벡터 사용)
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        
        // Base64 문자열을 바이트 배열로 디코딩
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        
        // 복호화 수행 후 UTF-8 문자열로 변환하여 반환
        return new String(c.doFinal(byteStr), "UTF-8");
    }

} // end of class AES256