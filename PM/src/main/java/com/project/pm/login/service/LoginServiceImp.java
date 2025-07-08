package com.project.pm.login.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.pm.employee.model.EmpVO;
import com.project.pm.login.model.LoginDAO;

@Service
public class LoginServiceImp implements LoginService{
	
	@Autowired
	private LoginDAO dao; 

	@Override
	public EmpVO checkLogin(Map<String, String> loginMap) {
		EmpVO loginuser = dao.checkLogin(loginMap);
		return loginuser;
	}

}
