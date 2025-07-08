package com.project.pm.schedule.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.project.pm.member.model.MemberVO;

@Repository
public class ScheduleDAOImp implements ScheduleDAO{

	@Resource
	private SqlSessionTemplate sqlsession;
	
	@Override
	public List<MemberVO> searchJoinUserList(String joinuser) {
		List<MemberVO> searchJoinUserList = sqlsession.selectList("manage.searchJoinUserList", joinuser);
		return searchJoinUserList;
	}

	// 켈린더에 일정을 등록하는 메소드
	@Override
	public int insertSchedule(ScheduleVO scvo) {
		int n = sqlsession.insert("manage.insertSchedule", scvo);
		return n;
	}

	// 캘린더의 일정을 모두 가져오는 메소드
	@Override
	public List<ScheduleVO> selectScheduleList() {
		List<ScheduleVO> selectScheduleList = sqlsession.selectList("manage.selectScheduleList");
		return selectScheduleList;
	}

	// 특정 하나 켈린더를 가져오는 메소드
	@Override
	public ScheduleVO selectDetailSchedule(String schedule_no) {
		ScheduleVO scvo = sqlsession.selectOne("manage.selectDetailSchedule", schedule_no);
		return scvo;
	}

	// 켈린더에 일정을 수정하는 메소드
	@Override
	public int updateSchedule(ScheduleVO scvo) {
		int n = sqlsession.update("manage.updateSchedule", scvo);
		return n;
	}

	// 캘린더를 삭제하는 메소드
	@Override
	public int deleteSchedule(String schedule_no) {
		int n = sqlsession.delete("manage.deleteSchedule", schedule_no);
		return n;
	}

	// 시퀀스 넘버를 가져오는 메소드
	@Override
	public String getSequenceNo() {
		String seq_no = sqlsession.selectOne("manage.getSequenceNo");
		return seq_no;
	}
	
	// 휴가신청후 스케줄테이블에 등록하는 메소드
	@Override
	public void insertVactionSchedule(Map<String, String> paraMap) {
		sqlsession.insert("manage.insertVactionSchedule",paraMap);
	}

	// 휴가취소후 스케줄테이블에서 삭제하는 메소드
	@Override
	public void deleteVactionSchedule(Map<String, String> paraMap) {
		sqlsession.delete("manage.deleteVactionSchedule", paraMap);
		
	}
}
