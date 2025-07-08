package com.project.pm.schedule.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ScheduleVO {
	
	private String schedule_no; 
	private String fk_empno;
	private String start_date;
	private String end_date;
	private String subject;
	private String content;
	private String color;
	private String category;
	private String fk_deptno;
	private String joinuser;
	private String place;
	
	// select 용 field
	private String birthday;
	private String name;

}
