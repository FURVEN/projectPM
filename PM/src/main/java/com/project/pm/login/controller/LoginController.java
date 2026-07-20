package com.project.pm.login.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.pm.employee.model.EmpVO;
import com.project.pm.login.model.LoginDAO;
import com.project.pm.login.service.LoginService;

/**
 * 로그인 관련 기능을 처리하는 컨트롤러
 * 
 * 이 컨트롤러는 프로젝트의 메인 진입점 역할을 하며, 다음 기능들을 제공합니다:
 * - 메인 페이지 접근 제어 (로그인 상태 확인)
 * - 로그인 페이지 표시
 * - 로그인 인증 처리 (AJAX)
 * - 로그아웃 처리
 * - 비밀번호 찾기
 */
@Controller
public class LoginController {
	
	/**
	 * 로그인 비즈니스 로직 처리 서비스
	 */
	@Autowired
	private LoginService service;
	
	/**
	 * 로그인 데이터 접근 객체 (DAO)
	 */
	@Autowired
	private LoginDAO dao;
	
	/**
	 * 애플리케이션 메인 진입점
	 * 
	 * 처리 흐름:
	 * 1. 세션에서 로그인 사용자 정보 확인
	 * 2. 로그인된 경우: 공지사항 목록 페이지로 리다이렉트
	 * 3. 비로그인 상태: 로그인 페이지로 리다이렉트
	 * 
	 * @param request HTTP 요청 객체 (세션 정보 획득용)
	 * @return 로그인 상태에 따른 리다이렉트 경로
	 */
	@RequestMapping(value = "/")
	public String main(HttpServletRequest request) {
		
		// 현재 HTTP 세션 획득
		HttpSession session = request.getSession();
		
		// 세션에 로그인 사용자 정보가 있는지 확인
		if(session.getAttribute("loginuser") != null) {
			// 로그인된 상태: 공지사항 목록 페이지로 리다이렉트
			return "redirect:/notice/noticeList.pm";
		}
		else {
			// 비로그인 상태: 로그인 페이지로 리다이렉트
			return "redirect:login.pm";
		}
	}
	
	/**
	 * 로그인 페이지 표시
	 * 
	 * @return 로그인 뷰 페이지 경로 (login.login)
	 */
	@RequestMapping(value = "/login.pm")
	public String login() {
		
		// Apache Tiles 설정에 따른 뷰 이름 반환
		// "login.login"은 login 카테고리의 login 페이지를 의미
		return "login.login";
	}
	
	/**
	 * 로그아웃 처리
	 * 
	 * 처리 흐름:
	 * 1. 현재 세션 획득
	 * 2. 세션에서 로그인 사용자 정보 제거
	 * 3. 로그인 페이지로 리다이렉트
	 * 
	 * @param request HTTP 요청 객체 (세션 정보 획득용)
	 * @return 로그인 페이지로의 리다이렉트 경로
	 */
	@RequestMapping(value = "/logout.pm")
	public String logout(HttpServletRequest request) {
		
		// 현재 HTTP 세션 획득
		HttpSession session = request.getSession();
		
		// 세션에서 로그인 사용자 정보 제거 (로그아웃 처리)
		session.removeAttribute("loginuser");
		
		// 로그인 페이지로 리다이렉트
		return "redirect:/login.pm";
	}
	
	/**
	 * AJAX를 통한 로그인 인증 처리
	 * 
	 * 처리 흐름:
	 * 1. 클라이언트로부터 이메일과 비밀번호 파라미터 수신
	 * 2. 로그인 정보를 Map에 저장하여 서비스로 전달
	 * 3. 서비스를 통해 사용자 인증 수행
	 * 4. 인증 성공 시 세션에 사용자 정보 저장
	 * 5. 인증 결과를 JSON 형태로 반환
	 * 
	 * @param request HTTP 요청 객체 (파라미터 수신용)
	 * @return JSON 형태의 인증 결과 {"result": true/false}
	 */
	@ResponseBody  // JSON 응답을 위한 어노테이션
	@RequestMapping(value = "/loginContinue.pm")
	public String loginContinue(HttpServletRequest request) {
		// 인증 결과 플래그 초기화
		boolean result = false;
		
		// 클라이언트로부터 로그인 정보 파라미터 수신
		String email = request.getParameter("email"); // 사용자 이메일 (아이디)
		String pwd = request.getParameter("pwd");     // 사용자 비밀번호
		
		// 로그인 정보를 Map에 저장 (서비스 계층으로 전달하기 위함)
		Map<String,String> loginMap = new HashMap<String, String>();
		
		loginMap.put("email", email);
		loginMap.put("pwd", pwd);
		
		// 개발/디버깅용 주석처리된 로그
		// System.out.println("~~~ 확인용 email = "+email);
		// System.out.println("~~~ 확인용 pwd = "+pwd);
		
		// 서비스를 통해 로그인 인증 수행
		EmpVO loginuser = service.checkLogin(loginMap);
		
		// 인증 결과 처리
		if(loginuser != null) {
			// 인증 성공: 세션에 사용자 정보 저장
			HttpSession session = request.getSession();
			session.setAttribute("loginuser", loginuser);
			result = true;
			
		}
		// 인증 실패: result는 false로 유지
		
		// JSON 응답 객체 생성
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", result);
		
		// JSON 문자열로 변환하여 반환
		return jsonObj.toString();
		
	}
		
	/**
	 * 로그인 관련 AJAX 처리 (현재 미구현)
	 * 
	 * 이 메서드는 현재 구체적인 구현이 없고 빈 JSON 배열만 반환합니다.
	 * 향후 추가적인 로그인 관련 기능 구현 시 사용될 것으로 보입니다.
	 * 
	 * @param request HTTP 요청 객체
	 * @return 빈 JSON 배열 문자열
	 */
	@ResponseBody  // JSON 응답을 위한 어노테이션
	@RequestMapping(value = "/loginpw.pm")
	public String loginpw(HttpServletRequest request) {
		
		// 현재는 빈 JSON 배열만 반환 (향후 기능 구현 예정으로 보임)
		JSONArray jsonArr = new JSONArray();
		return jsonArr.toString();
	}
	
	/**
	 * 비밀번호 찾기 페이지 처리
	 * 
	 * 처리 흐름:
	 * 1. 클라이언트로부터 사용자 ID 파라미터 수신
	 * 2. 받은 사용자 ID를 request 속성에 저장
	 * 3. 비밀번호 찾기 페이지로 이동
	 * 
	 * @param request HTTP 요청 객체 (파라미터 수신 및 속성 저장용)
	 * @return 비밀번호 찾기 뷰 페이지 경로 (finPW.login)
	 */
	@RequestMapping(value = "/findPW.pm")
	public String findPW(HttpServletRequest request) {
		
		// 클라이언트로부터 사용자 ID 파라미터 수신
		String userid = request.getParameter("userid");
		
		// 받은 사용자 ID를 request 속성에 저장 (뷰에서 사용하기 위함)
		request.setAttribute("userid", userid);
		
		// Apache Tiles 설정에 따른 비밀번호 찾기 뷰 이름 반환
		// "finPW.login"은 login 카테고리의 finPW 페이지를 의미
		return "finPW.login";
	}
	

}
