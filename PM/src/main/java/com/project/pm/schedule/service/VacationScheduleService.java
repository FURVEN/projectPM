package com.project.pm.schedule.service;

import java.util.Map;

public interface VacationScheduleService {
	
		// 휴가등록시 스케줄테이블에 insert 해주기
		void addSchedule(Map<String, String> paraMap);

		// 휴가취소시 스케줄테이블에서 삭제
		void delSchedule(Map<String, String> paraMap);

}
