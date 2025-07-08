package com.project.pm.chart.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.pm.chart.model.ChartDAO;

@Service
public class ChartServiceImp implements ChartService{
	
	@Autowired
	private ChartDAO dao;

	// 전체 부서명을 조회해오는 메소드 
		@Override
		public List<Map<String, String>> deptName() {
			List<Map<String,String>> deptNameList = dao.deptName();
			return deptNameList;
		}

		// 부서번호를 전달받아 해당하는 팀 번호를 조회해오는 메소드
		@Override
		public List<Map<String, String>> getTeamName(Map<String, String> deptnoMap) {
			List<Map<String, String>> teamNameList = dao.getTeamName(deptnoMap);
			return teamNameList;
		}

		// 팀 번호를 전달 받아 해당하는 해당 팀에 존재하는 사원목록을 조회해오는 메소드
		@Override
		public List<Map<String, String>> getEmployees(Map<String, String> teamnoMap) {
			List<Map<String, String>> employeeList = dao.getEmployee(teamnoMap);
			return employeeList;
		}

}