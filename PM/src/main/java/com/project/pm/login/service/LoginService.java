package com.project.pm.login.service;

import java.util.Map;

import com.project.pm.employee.model.EmpVO;

/**
 * 로그인 서비스 인터페이스
 * 
 * 로그인 관련 비즈니스 로직을 정의하는 서비스 계층의 인터페이스입니다.
 * Spring의 Service 계층 패턴을 따라 컨트롤러와 DAO 사이의 중간 계층 역할을 합니다.
 * 
 * 이 인터페이스는 다음과 같은 역할을 수행합니다:
 * - 로그인 비즈니스 로직 추상화
 * - 컨트롤러와 데이터 접근 계층 간의 의존성 분리
 * - 향후 다양한 구현체로 확장 가능한 구조 제공
 */
public interface LoginService {
	
	/**
	 * 사용자 로그인 인증을 수행하는 메서드
	 * 
	 * 이 메서드는 사용자가 입력한 로그인 정보를 받아서 데이터베이스를 통해
	 * 인증을 수행하고, 인증된 사용자 정보를 반환합니다.
	 * 
	 * @param loginMap 로그인 정보가 담긴 Map 객체
	 *                 - "email": 사용자 이메일 (로그인 ID)
	 *                 - "pwd": 사용자 비밀번호
	 * @return EmpVO 인증된 직원 정보 객체 (인증 실패 시 null 반환)
	 */
	EmpVO checkLogin(Map<String, String> loginMap);

}
