package com.project.pm.employee.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class InsertVO {
	
	private String name;
	private String email;
	private String hiredate;
	private String time_salary;
	private String fk_deptno;
	private String position;

}
