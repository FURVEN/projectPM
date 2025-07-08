package com.project.pm.commute.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommuteVO {
	
	private String commuteno;           
	private String fk_empno;
	private String start_work_time;
	private String end_work_time;
	private String worktime;
	private String overtime;
	
	// select 용 field
	private String dt; // select 용 날짜

}
