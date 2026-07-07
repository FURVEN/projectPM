package com.project.pm.login.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.pm.common.SessionConst;
import com.project.pm.employee.model.EmpVO;
import com.project.pm.login.service.LoginService;

/**
 * 로그인/로그아웃/비밀번호 찾기 컨트롤러.
 *
 * 응답 JSON 형태는 기존 JSP(AJAX)와의 호환을 위해 유지한다.
 * (login.jsp 는 /loginContinue.pm 응답에서 json.result 만 사용한다)
 */
@Controller
public class LoginController {

	private final LoginService service;

	public LoginController(LoginService service) {
		this.service = service;
	}

	/**
	 * 메인 진입점: 로그인 상태면 공지사항 목록으로, 아니면 로그인 페이지로 보낸다.
	 */
	@RequestMapping(value = "/")
	public String main(HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (session.getAttribute(SessionConst.LOGIN_USER) != null) {
			return "redirect:/notice/noticeList.pm";
		}
		return "redirect:login.pm";
	}

	@RequestMapping(value = "/login.pm")
	public String login() {
		// Tiles 정의명 (login 레이아웃의 login 페이지)
		return "login.login";
	}

	/**
	 * 로그아웃. 세션 전체를 무효화하여 로그인 사용자 외에 남아 있을 수 있는 세션 속성도 함께 정리한다.
	 */
	@RequestMapping(value = "/logout.pm")
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login.pm";
	}

	/**
	 * AJAX 로그인 인증.
	 *
	 * 응답: {"result": true|false} — Jackson 이 직렬화하며, 기존 수동 JSON 문자열과 동일한 형태이다.
	 * email/pwd 가 비어 있으면 DB 조회 없이 실패로 처리한다.
	 */
	@ResponseBody
	@RequestMapping(value = "/loginContinue.pm")
	public Map<String, Object> loginContinue(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "pwd", required = false) String pwd,
			HttpServletRequest request) {

		boolean result = false;

		if (hasText(email) && hasText(pwd)) {
			Map<String, String> loginMap = new HashMap<String, String>();
			loginMap.put("email", email);
			loginMap.put("pwd", pwd);

			EmpVO loginuser = service.checkLogin(loginMap);

			if (loginuser != null) {
				HttpSession session = request.getSession();
				session.setAttribute(SessionConst.LOGIN_USER, loginuser);
				result = true;
			}
		}

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("result", result);
		return response;
	}

	/**
	 * 로그인 페이지에서 아이디 입력 후 비밀번호 영역을 표시하기 위해 호출되는 AJAX 엔드포인트.
	 * login.jsp 는 응답 본문을 사용하지 않으므로(성공 콜백에서 정적 HTML 생성)
	 * 기존과 동일하게 빈 JSON 배열을 반환한다.
	 */
	@ResponseBody
	@RequestMapping(value = "/loginpw.pm")
	public List<Object> loginpw() {
		return Collections.emptyList();
	}

	/**
	 * 비밀번호 찾기 페이지.
	 */
	@RequestMapping(value = "/findPW.pm")
	public String findPW(@RequestParam(value = "userid", required = false) String userid,
			HttpServletRequest request) {

		request.setAttribute("userid", userid);

		// Tiles 정의명 (login 레이아웃의 finPW 페이지)
		return "finPW.login";
	}

	private static boolean hasText(String s) {
		return s != null && !s.trim().isEmpty();
	}

}
