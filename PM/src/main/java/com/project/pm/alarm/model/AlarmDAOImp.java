package com.project.pm.alarm.model;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AlarmDAOImp implements AlarmDAO{
	
	@Resource
	private SqlSessionTemplate sqlsession;

		// AOP 알람추가하기
		@Override
		public void addAlarm(String sql) {
			sqlsession.insert("service.insertAlarm", sql);
		}

		// 알람 조회하기
		@Override
		public List<AlarmVO> getAlarmList(String empno) {
			List<AlarmVO> alarmList = sqlsession.selectList("service.selectAlarm", empno);
			return alarmList;
		}
		
		// 알람 조회하기
		@Override
		public List<AlarmVO> getPastAlarmList(String empno) {
			List<AlarmVO> alarmList = sqlsession.selectList("service.selectPastAlarm", empno);
			return alarmList;
		}

		// 알람 읽기
		@Override
		public void readAlarm(String alarmno) {
			sqlsession.update("service.updateAlarm", alarmno);
		}
		
		// 모든 알람 읽기
		@Override
		public void readAllAlarm(String empno) {
			sqlsession.update("service.updateAllAlarm", empno);
			
		}

		// 안 읽은 소식 개수 알아오기
		@Override
		public String getUnreadAlarmCnt(String empno) {
			String n = sqlsession.selectOne("service.getUnreadAlarmCnt", empno);
			return n;
		}

}
