package com.project.pm.login.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.pm.employee.model.EmpVO;
import com.project.pm.login.model.LoginDAO;

/**
 * 로그인 서비스 구현 클래스
 * 
 * LoginService 인터페이스의 구현체로, 실제 로그인 비즈니스 로직을 처리합니다.
 * Spring의 @Service 어노테이션을 통해 서비스 빈으로 등록되어 의존성 주입이 가능합니다.
 * 
 * 이 클래스는 다음과 같은 역할을 수행합니다:
 * - LoginDAO를 통한 데이터베이스 접근
 * - 로그인 인증 로직 처리
 * - 컨트롤러와 DAO 사이의 중간 계층 역할
 */
@Service  // Spring 서비스 컴포넌트로 등록
public class LoginServiceImp implements LoginService{
	
	/**
	 * 로그인 데이터 접근 객체
	 * Spring의 의존성 주입을 통해 자동으로 LoginDAO 구현체가 주입됩니다.
	 */
	@Autowired
	private LoginDAO dao; 

	/**
	 * 사용자 로그인 인증 구현 메서드
	 * 
	 * 이 메서드는 LoginService 인터페이스의 checkLogin 메서드를 구현하며,
	 * DAO를 통해 실제 데이터베이스에서 사용자 인증을 수행합니다.
	 * 
	 * 처리 흐름:
	 * 1. LoginDAO의 checkLogin 메서드 호출
	 * 2. 데이터베이스에서 사용자 정보 조회 및 인증
	 * 3. 인증된 사용자 정보 반환 (실패 시 null)
	 * 
	 * @param loginMap 로그인 정보가 담긴 Map 객체
	 *                 - "email": 사용자 이메일 (로그인 ID)
	 *                 - "pwd": 사용자 비밀번호
	 * @return EmpVO 인증된 직원 정보 객체 (인증 실패 시 null 반환)
	 */
	@Override
	public EmpVO checkLogin(Map<String, String> loginMap) {
		// DAO를 통해 데이터베이스에서 로그인 인증 수행
		EmpVO loginuser = dao.checkLogin(loginMap);
		
		// 인증 결과 반환 (성공 시 EmpVO 객체, 실패 시 null)
		return loginuser;
	}

}
