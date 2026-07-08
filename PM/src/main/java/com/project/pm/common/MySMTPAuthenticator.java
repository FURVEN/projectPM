package com.project.pm.common;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

//==== #187. Spring Scheduler(스프링스케줄러6) ==== //
//==== Spring Scheduler(스프링스케줄러)를 사용한 email 발송하기 ====
//==== email을 보내주는 클래스 생성하기 ====
public class MySMTPAuthenticator extends Authenticator {

	@Override
	public PasswordAuthentication getPasswordAuthentication() {

		// 계정/앱비밀번호는 하드코딩하지 않고 시스템 프로퍼티 또는 환경변수에서 읽는다. (MailConfig 참조)
		return new PasswordAuthentication(MailConfig.smtpUser(), MailConfig.smtpPassword());
	}
	
}
