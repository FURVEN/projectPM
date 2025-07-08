package com.project.pm.schedule.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.pm.schedule.model.ScheduleDAO;

@Service
public class VacationScheduleServiceImp implements VacationScheduleService{
	
	@Autowired
	private ScheduleDAO scheduledao;
	
	@Override
	public void addSchedule(Map<String, String> paraMap) {
		
		scheduledao.insertVactionSchedule(paraMap);
		
		
	}

	
	@Override
	public void delSchedule(Map<String, String> paraMap) {
		
		String approval = paraMap.get("approval");
		
		// 반려일때만 삭제해주면 된다
		if("2".equals(approval)) {
			scheduledao.deleteVactionSchedule(paraMap);
		}
	}

}
