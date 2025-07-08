package com.project.pm.login.model;

import java.util.Map;

import com.project.pm.employee.model.EmpVO;

public interface LoginDAO {
	
	EmpVO checkLogin(Map<String, String> loginMap);

}
