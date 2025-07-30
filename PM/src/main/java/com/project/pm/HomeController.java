package com.project.pm;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 애플리케이션 홈페이지 요청을 처리하는 컨트롤러
 * 
 * 이 컨트롤러는 기본 홈페이지 요청을 처리합니다.
 * 현재는 @Controller 어노테이션이 주석처리되어 비활성화된 상태입니다.
 * 
 * 프로젝트에서는 LoginController가 메인 진입점 역할을 담당하고 있습니다.
 */
//@Controller  // 현재 비활성화됨 - LoginController가 메인 컨트롤러 역할
public class HomeController {
	
	/**
	 * 로거 인스턴스 - 애플리케이션 로그 기록용
	 */
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * 홈페이지 뷰를 반환하는 메서드
	 * 
	 * 처리 흐름:
	 * 1. 클라이언트의 로케일 정보를 로그에 기록
	 * 2. 현재 서버 시간을 포맷팅하여 모델에 추가
	 * 3. "home" 뷰 이름을 반환하여 home.jsp 페이지 렌더링
	 * 
	 * @param locale 클라이언트의 로케일 정보 (지역/언어 설정)
	 * @param model 뷰에 데이터를 전달하기 위한 모델 객체
	 * @return "home" - home.jsp 뷰 페이지로 이동
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		// 클라이언트 로케일 정보를 로그에 기록
		logger.info("Welcome home! The client locale is {}.", locale);
		
		// 현재 서버 시간 생성
		Date date = new Date();
		
		// 로케일에 맞는 날짜/시간 포맷터 생성 (긴 형식으로 표시)
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		// 날짜를 포맷팅하여 문자열로 변환
		String formattedDate = dateFormat.format(date);
		
		// 포맷팅된 서버 시간을 모델에 추가하여 뷰에서 사용할 수 있도록 함
		model.addAttribute("serverTime", formattedDate);
		
		// "home" 뷰 이름 반환 (home.jsp 페이지 렌더링)
		return "home";
	}
	
}
