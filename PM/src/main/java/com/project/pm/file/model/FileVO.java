package com.project.pm.file.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FileVO {
	
	private String fk_msgno;	// 원본 메세지 그룹넘버임
	private String pk_fileno;
	private String fileName;    // WAS(톰캣)에 저장될 파일명(2022042911123035243254235235234.png) 
	private String orgFilename; // 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명
	private String fileSize;    // 파일크기 

}
