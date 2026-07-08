package com.project.pm.login.model;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.project.pm.employee.model.EmpVO;

/**
 * 로그인 DAO 구현체. mapper/emp.xml 의 emp 네임스페이스 쿼리를 사용한다.
 */
@Repository
public class LoginDAOImp implements LoginDAO {

	private final SqlSessionTemplate sqlsession;

	public LoginDAOImp(SqlSessionTemplate sqlsession) {
		this.sqlsession = sqlsession;
	}

	/**
	 * @param loginMap "email", "pwd" 를 담은 Map
	 * @return 인증된 직원 정보, 일치하는 행이 없으면 null
	 */
	@Override
	public EmpVO checkLogin(Map<String, String> loginMap) {
		return sqlsession.selectOne("emp.checkLogin", loginMap);
	}

}
