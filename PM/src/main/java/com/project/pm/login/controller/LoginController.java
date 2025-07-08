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

@Controller
public class LoginController {
	
	@Autowired
	private LoginService service;
	
	@Autowired
	private LoginDAO dao;
	
	@RequestMapping(value = "/")
	public String main(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loginuser") != null) {
			return "redirect:/notice/noticeList.pm";
		}
		else {
			return "redirect:login.pm";
		}
	}
	
	@RequestMapping(value = "/login.pm")
	public String login() {
		
		return "login.login";
	}
	
	// 로그아웃 
	@RequestMapping(value = "/logout.pm")
	public String logout(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		session.removeAttribute("loginuser");
		
		return "redirect:/login.pm";
	}
	
	//ajax
	@ResponseBody
	@RequestMapping(value = "/loginContinue.pm")
	public String loginContinue(HttpServletRequest request) {
		boolean result = false;
		
		String email = request.getParameter("email"); // 아이디
		String pwd = request.getParameter("pwd");     // 비밀번호 
		
		Map<String,String> loginMap = new HashMap<String, String>();
		
		loginMap.put("email", email);
		loginMap.put("pwd", pwd);
		
		// System.out.println("~~~ 확인용 email = "+email);
		// System.out.println("~~~ 확인용 pwd = "+pwd);
		
		EmpVO loginuser = service.checkLogin(loginMap);
		
		if(loginuser != null) {
			HttpSession session = request.getSession();
			session.setAttribute("loginuser", loginuser);
			result = true;
			
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", result);
		
		return jsonObj.toString();
		
	}
		
	//ajax
	@ResponseBody
	@RequestMapping(value = "/loginpw.pm")
	public String loginpw(HttpServletRequest request) {
		

		JSONArray jsonArr = new JSONArray();
		return jsonArr.toString();
	}
	
	@RequestMapping(value = "/findPW.pm")
	public String findPW(HttpServletRequest request) {
		
		String userid = request.getParameter("userid");
		
		request.setAttribute("userid", userid);
		
		return "finPW.login";
	}
	

}
