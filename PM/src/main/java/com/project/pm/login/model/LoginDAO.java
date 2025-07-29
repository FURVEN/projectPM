package com.project.pm.login.model;

import java.util.Map;

import com.project.pm.employee.model.EmpVO;

/**
 * 로그인 데이터 접근 객체(DAO) 인터페이스
 * 
 * 로그인 관련 데이터베이스 작업을 정의하는 DAO 계층의 인터페이스입니다.
 * MyBatis를 통한 데이터베이스 접근 작업을 추상화하여 정의합니다.
 * 
 * 이 인터페이스는 다음과 같은 역할을 수행합니다:
 * - 로그인 관련 데이터베이스 작업 추상화
 * - 서비스 계층과 데이터 접근 계층 간의 의존성 분리
 * - 향후 다양한 데이터 접근 방식으로 확장 가능한 구조 제공
 */
public interface LoginDAO {
	
	/**
	 * 사용자 로그인 인증을 위한 데이터베이스 조회 메서드
	 * 
	 * 입력받은 로그인 정보(이메일, 비밀번호)를 바탕으로 데이터베이스에서
	 * 해당 사용자의 정보를 조회하여 인증을 수행합니다.
	 * 
	 * @param loginMap 로그인 정보가 담긴 Map 객체
	 *                 - "email": 사용자 이메일 (로그인 ID)
	 *                 - "pwd": 사용자 비밀번호
	 * @return EmpVO 인증된 직원 정보 객체 (인증 실패 시 null 반환)
	 */
	EmpVO checkLogin(Map<String, String> loginMap);

}
