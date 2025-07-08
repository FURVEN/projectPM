package com.project.pm.leave.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LeaveDAOImp implements LeaveDAO{

	@Resource
	private SqlSessionTemplate sqlsession;

	// 휴가 종류와 남은 일수 구해오기
	@Override
	public List<Map<String, String>> getLeaveTypeList(Map<String, String> paraMap) {
		List<Map<String, String>> leaveTypeList = sqlsession.selectList("service.selectLeaveType",paraMap);
		return leaveTypeList;
	}

	
	// 모달에 들어갈 해당 휴가에 대한 상세 내용 알아오기
	@Override
	public Map<String, String> getLeaveDate(Map<String, String> paraMap) {
		Map<String, String> leaveData = sqlsession.selectOne("service.selectLeaveData",paraMap);
		return leaveData;
	}


	// 휴가 신청하기
	@Override
	public void requestLeave(Map<String, String> parameterMap) {
		sqlsession.insert("service.insertRequestLeave",parameterMap);
	}

	
	// 휴가 신청하면서 사용내역 업데이트 하기
	@Override
	public void plusUsedLeave(Map<String, String> parameterMap) {
		sqlsession.update("service.plusUsedLeave",parameterMap);
	}


	// 연차 사용내역 업데이트 
	@Override
	public void plusUsedLeaveAnnual(Map<String, String> parameterMap) {
		sqlsession.update("service.plusUsedLeaveAnnual",parameterMap);
	}

	// 반차 사용 내역 업데이트
	@Override
	public void plusUsedLeaveHalf_annual(Map<String, String> parameterMap) {
		sqlsession.update("service.plusUsedLeaveHalf_annual",parameterMap);
	}


	// 휴가 사용 내역 조회하기
	@Override
	public List<Map<String, String>> getLeaveRecode(Map<String, String> paraMap) {
		List<Map<String, String>> leaveRecodeList = sqlsession.selectList("service.getLeaveRecode", paraMap);
		return leaveRecodeList;
	}


	// 휴가 예정 내역 조회하기
	@Override
	public List<Map<String, String>> getLeavePlan(Map<String, String> paraMap) {
		List<Map<String, String>> leavePlanList = sqlsession.selectList("service.getLeavePlan", paraMap);
		return leavePlanList;
	}


	// 하위 부서 번호 전부 알아오기
	@Override
	public List<String> getLowerDeptnoList(String fk_deptno) {
		List<String> lowerDeptnoList = sqlsession.selectList("service.getLowerDeptnoList", fk_deptno);
		return lowerDeptnoList;
	}


	// 조회한 부서에 해당하는 사원들의 휴가 신청내역을 불러온다
	@Override
	public List<Map<String, String>> getRequestLeaveList(Map<String, String> paraMap) {
		List<Map<String,String>> requestLeaveList = sqlsession.selectList("service.getRequestLeaveList", paraMap);
		return requestLeaveList;
	}


	// 조회한 부서에 해당하는 사원들의 휴가 사용/잔여 내역을 불러온다
	@Override
	public List<Map<String, String>> getLeaveStatusList(String deptJoin) {
		List<Map<String,String>> leaveStatusList = sqlsession.selectList("service.getLeaveStatusList", deptJoin);
		return leaveStatusList;
	}


	// 관리자 사원번호 리스트 가져오기
	@Override
	public List<String> getAdminEmpnoList() {
		List<String> adminEmpnoList = sqlsession.selectList("service.getAdminEmpnoList");
		return adminEmpnoList;
	}


	// 휴가신청 번호로 휴가신청 상세 조회
	@Override
	public Map<String, String> getLeaveRequestDetail(String request_leaveno) {
		Map<String, String> leaveRequestDetail = sqlsession.selectOne("service.getLeaveRequestDetail", request_leaveno);
		return leaveRequestDetail;
	}


	// 휴가신청에 파일 추가 하기
	@Override
	public void addFileToRequestLeave(Map<String, String> paraMap) {
		sqlsession.update("service.addFileToRequestLeave", paraMap);
	}


	//휴가신청 삭제하기
	@Override
	public void deleteRequestLeave(Map<String, String> leaveRequestDetail) {
		sqlsession.delete("service.deleteRequestLeave", leaveRequestDetail);
	}

	// 휴가 신청 삭제하면서 사용 내역들도 같이 줄여주기
	@Override
	public void minusUsedLeaveAnnual(Map<String, String> leaveRequestDetail) {
		sqlsession.update("service.minusUsedLeaveAnnual", leaveRequestDetail);
	}
	@Override
	public void minusUsedLeaveHalf_annual(Map<String, String> leaveRequestDetail) {
		sqlsession.update("service.minusUsedLeaveHalf_annual", leaveRequestDetail);
	}
	@Override
	public void minusUsedLeave(Map<String, String> leaveRequestDetail) {
		sqlsession.update("service.minusUsedLeave", leaveRequestDetail);
	}


	// 승인 / 반려하기 함수 
	@Override
	public void approvalRequestLevae(Map<String, String> leaveRequestDetail) {
		sqlsession.update("service.approvalRequestLevae", leaveRequestDetail);
	}

}
