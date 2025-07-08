package com.project.pm.login.service;

import java.util.Map;

import com.project.pm.employee.model.EmpVO;

public interface LoginService {
	
	EmpVO checkLogin(Map<String, String> loginMap);

}
