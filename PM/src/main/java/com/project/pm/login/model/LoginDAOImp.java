package com.project.pm.login.model;

import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.project.pm.employee.model.EmpVO;

@Repository
public class LoginDAOImp implements LoginDAO{
	
	@Resource
	private SqlSessionTemplate sqlsession; 

	@Override
	public EmpVO checkLogin(Map<String, String> loginMap) {
		EmpVO loginuser = sqlsession.selectOne("emp.checkLogin",loginMap);
		return loginuser;
	}

}
