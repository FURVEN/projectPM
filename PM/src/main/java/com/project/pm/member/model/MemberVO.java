package com.project.pm.member.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MemberVO {
	
	private String empno;		// 사원번호
	private String deptno;		// 부서번호
	private String email; 	// 사원아이디(이메일)  
	private String pwd;		// 비밀번호
	private String name; 	// 이름
	private String hiredate;	// 입사일
	private String retiredate;    
	private String birthdate;      
	private String mobile;         
	private String address;          
	private String manger_no;     
	private String position;       
	private String time_salary;   
	private String status;

}
