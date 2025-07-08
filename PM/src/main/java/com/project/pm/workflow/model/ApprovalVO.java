package com.project.pm.workflow.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApprovalVO {
	
	private int approval_no; 		//결재번호
	 private int fk_doc_no;   		//문서번호
	 private int fk_senior_empno; //결제할 사원번호
	 private int levelno; 			// 결제단계번호
	 private int approval; 			// 0:승인 1: 허가 -1:반려
	 private String comments; 		// 코멘트
	 private String approval_day;  	// 승인 일자

}
