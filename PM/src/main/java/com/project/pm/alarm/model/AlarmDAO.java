package com.project.pm.alarm.model;

import java.util.List;

public interface AlarmDAO {
	
		// AOP 알람추가하기
		void addAlarm(String sql);

		// 알람 조회하기
		List<AlarmVO> getAlarmList(String empno);
		
		// 알람 조회하기
		List<AlarmVO> getPastAlarmList(String empno);

		// 알람 읽기
		void readAlarm(String alarmno);

		// 모든 알람 읽기
		void readAllAlarm(String empno);

		// 안 읽은 소식 개수 알아오기
		String getUnreadAlarmCnt(String empno);

}
