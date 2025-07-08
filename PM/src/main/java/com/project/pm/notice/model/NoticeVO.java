package com.project.pm.notice.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NoticeVO {
	
	private String notino;
	private String fk_senderno;
	private String fk_deptno;
	private String notiLevel;
	private String writedate;
	private String subject;
	private String content;
	private String readCount;
	private String cmtCount;
	private String status;
	private String fileName;
	private String orgFilename;
	private String fileSize;
	
	private MultipartFile attach;
	
	public NoticeVO() {}

	public NoticeVO(String notino, String fk_senderno, String fk_deptno, String notiLevel, String writedate,
			String subject, String content, String readCount, String cmtCount, String status, String fileName,
			String orgFilename, String fileSize) {
		super();
		this.notino = notino;
		this.fk_senderno = fk_senderno;
		this.fk_deptno = fk_deptno;
		this.notiLevel = notiLevel;
		this.writedate = writedate;
		this.subject = subject;
		this.content = content;
		this.readCount = readCount;
		this.cmtCount = cmtCount;
		this.status = status;
		this.fileName = fileName;
		this.orgFilename = orgFilename;
		this.fileSize = fileSize;
	}
}
