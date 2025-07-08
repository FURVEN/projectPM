package com.project.pm.chart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.pm.chart.service.ChartService;

@Controller
public class ChartController {
	
	@Autowired
	private ChartService service;
	
	// 조직도(차트) 출력 페이지 
		@RequestMapping(value = "/organization_chart.pm")
		public String organization_chart() {
			return "emp/organization_chart.admin";
		}
		
		
		// 전체 부서 이름 조회하기
		@ResponseBody
		@RequestMapping(value="/getDeptName.pm", produces="text/plain;charset=UTF-8")
		public String deptName() {
			
			List<Map<String,String>> deptNameList = service.deptName();
			JsonArray jsonArr = new JsonArray();
			
			if(deptNameList != null) {
				for( Map<String,String> deptNameMap : deptNameList ) {
					JsonObject jsonObj = new JsonObject();
					jsonObj.addProperty("deptname", deptNameMap.get("deptname")); // 부서명
					jsonObj.addProperty("deptno", deptNameMap.get("deptno")); // 부서번호 
					jsonObj.addProperty("upper_deptno", deptNameMap.get("upper_deptno")); // 상위부서번호 (1)
					
					jsonArr.add(jsonObj);
				}
			}
			return new Gson().toJson(jsonArr);
		}
		
		// 전체 팀 이름 조회하기
		@ResponseBody
		@RequestMapping(value="/getTeamName.pm", produces="text/plain;charset=UTF-8")
		public String teamName(HttpServletRequest request) {
			
			String deptno = request.getParameter("deptno");
			Map<String,String> deptnoMap = new HashMap<>();
			deptnoMap.put("deptno", deptno);
			
			List<Map<String,String>> teamNameList = service.getTeamName(deptnoMap);
			JsonArray jsonArr = new JsonArray();
			
			if(teamNameList != null) {
				for( Map<String,String> teamNameMap : teamNameList ) {
					
					JsonObject jsonObj = new JsonObject();
					
					jsonObj.addProperty("teamname", teamNameMap.get("deptname")); // 팀명
					jsonObj.addProperty("teamno", teamNameMap.get("deptno"));     // 팀번호 
					jsonObj.addProperty("upper_deptno", teamNameMap.get("upper_deptno")); // 상위부서번호 (10~30)
					
					jsonArr.add(jsonObj);
				}
			}
			return new Gson().toJson(jsonArr);
		}
		
		// 특정 팀에 속하는 사원 조회하기 
		@ResponseBody
		@RequestMapping(value="/getEmployees.pm", produces="text/plain;charset=UTF-8")
		public String getEmployees(HttpServletRequest request) {
			
			String teamno = request.getParameter("teamno");
			Map<String,String> teamnoMap = new HashMap<>();
			teamnoMap.put("teamno", teamno);
			
			List<Map<String,String>> employeeList = service.getEmployees(teamnoMap);
			JsonArray jsonArr = new JsonArray();
			
			if(employeeList != null) {
				for( Map<String,String> employeeMap : employeeList ) {
					
					JsonObject jsonObj = new JsonObject();
					jsonObj.addProperty("name", employeeMap.get("name")); // 팀명
					jsonObj.addProperty("deptname", employeeMap.get("deptname")); // 팀명
					jsonArr.add(jsonObj);
				}
			}
			return new Gson().toJson(jsonArr);
		}

}
