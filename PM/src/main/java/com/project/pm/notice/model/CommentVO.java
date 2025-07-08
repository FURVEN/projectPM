package com.project.pm.notice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommentVO {
	
	private String commentno;
	private String fk_empno;
	private String fk_notino;
	private String content;
	private String writedate;
	
	

}
