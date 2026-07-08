package com.project.pm.login.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.pm.employee.model.EmpVO;
import com.project.pm.login.model.LoginDAO;

/**
 * 로그인 서비스 구현체. 인증 조회는 LoginDAO 에 위임한다.
 */
@Service
public class LoginServiceImp implements LoginService {

	private final LoginDAO dao;

	public LoginServiceImp(LoginDAO dao) {
		this.dao = dao;
	}

	/**
	 * @param loginMap "email", "pwd" 를 담은 Map
	 * @return 인증된 직원 정보, 인증 실패 시 null
	 */
	@Override
	public EmpVO checkLogin(Map<String, String> loginMap) {
		return dao.checkLogin(loginMap);
	}

}
