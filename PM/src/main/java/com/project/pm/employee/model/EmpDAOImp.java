package com.project.pm.employee.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmpDAOImp implements EmpDAO{
	
	@Resource
	private SqlSessionTemplate sqlsession; 

	// 전체 사원을 조회해오는 메소드 
		@Override
		public List<Map<String, String>> getEmpList(Map<String, String> empMap) {
			
			List<Map<String, String>> empList = sqlsession.selectList("emp.getEmpList", empMap);
			return empList;
		}
		
		// 사원 번호를 전달받아 사원 한명의 정보를 조회해오는 메소드
		@Override
		public Map<String, String> getEmpOne(Map<String, String> empnoMap) {
			Map<String, String> employeeMap = sqlsession.selectOne("emp.getEmpOne",empnoMap);
			return employeeMap;
		}

		// 시작일, 종료일, 사원번호, 메모, 사원번호를 Map 으로 전달받아 휴직테이블에 insert 하는 메소드
		@Override
		public int insertLeave(Map<String, String> leaveMap) {
			int result = sqlsession.insert("emp.insertLeave",leaveMap);
			return result;
		}
		
		// 사원번호를 Map 으로 전달받아 휴직 상태로 update 하는 메소드 
		@Override
		public int updateLeave(Map<String, String> leaveMap) {
			int leaveUpdate = sqlsession.update("emp.updateLeave", leaveMap);
			return leaveUpdate;
		}

		
		// 사원번호, 휴직시작일,휴직종료일을 Map 으로 전달받아 가능한 기간인지 조회하는 메소드
		@Override
		public int checkLeave(Map<String, String> leaveCheckMap) {
			int result = sqlsession.selectOne("emp.leaveCheck",leaveCheckMap);
			return result;
		}
		

		// 재직 처리해야 할 사원들의 목록을 조회하는 메소드  
		@Override
		public List<Map<String, String>> getLeaveEmpList() {
			List<Map<String, String>> leaveEmpList = sqlsession.selectList("emp.getLeaveEmpList");
			return leaveEmpList;
		}

		// 휴직 처리해야할 사원들의 목록을 조회하는 메소드 
		@Override
		public List<Map<String, String>> getLeaveStartEmpList() {
			List<Map<String, String>> leaveStartEmpList = sqlsession.selectList("emp.getLeaveStartEmpList");
			return leaveStartEmpList;
		}

		// 휴직중인 사원의 목록을 전달받아 배열로 변환 후 재직 상태로 update 하는 메소드
		@Override
		public void updateLeaveEmp(Map<String, String[]> paraMap) {
			sqlsession.selectList("emp.updateLeaveEmp",paraMap);
			
		}

		// 재직중인 사원의 목록을 전달받아 배열로 변환 후 휴직 상태로 update 하는 메소드
		@Override
		public void updateLeaveStartEmp(Map<String, String[]> paraMap) {
			sqlsession.selectList("emp.updateLeaveStartEmp",paraMap);
			
		}
		
		// 전체 부서번호, 부서명 조회하는 메소드 
		@Override
		public List<Map<String, String>> getDeptList() {
			String deptno = "1";
			List<Map<String, String>> deptList = sqlsession.selectList("emp.selectDept", deptno);
			return deptList;
		}
		
		
		// 해당부서 팀 구해오기
		@Override
		public List<Map<String, String>> getTeam(Map<String, String> deptMap) {
			List<Map<String, String>> teamList = sqlsession.selectList("emp.getTeamName", deptMap);
			return teamList;
		}

		// 페이징 처리를 위해 총 사원 수 구해오기 
		@Override
		public int getTotalCount(Map<String, Object> empMap) {
			int totalCount = sqlsession.selectOne("emp.getTotalCount",empMap);
			return totalCount;
		}

		// 페이징 처리한 글목록 가져오기 (검색이 있든지, 검색이 없든지 모두 다 포함한 것)
		@Override
		public List<Map<String, String>> empListSearchWithPaging(Map<String, Object> pageMap) {
			List<Map<String,String>> empListPaging = sqlsession.selectList("emp.empListSearchWithPaging", pageMap);
			return empListPaging;
		}

		// 페이징 처리를 위해 총 페이지 수 구해오기 
		@Override
		public int getTotalPage(Map<String, Object> pageMap) {
			int totalPage = sqlsession.selectOne("emp.getTotalPage",pageMap);
			return totalPage;
		}

		// rno 에 해당하는 사원 목록 가져오기 
		@Override
		public List<Map<String, String>> empListWithRno(Map<String, String> pageMap) {
			List<Map<String,String>> empList = sqlsession.selectList("emp.empListWithRno",pageMap);
			return empList;
		}

		// 회원가입시 이메일 중복 여부 확인 
		@Override
		public int checkDuplicateEmail(Map<String, Object> paraMap) {
			int duplicateEmail = sqlsession.selectOne("emp.checkDuplicateEmail",paraMap);
			return duplicateEmail;
		}

		// 신규 사원 등록 
		@Override
		public int registEployee(Map<String, Object> paraMap) {
			int registResult = sqlsession.insert("emp.registEmployee", paraMap);
			return registResult;
		}

		// 사원번호, 월요일(날짜), 금요일(날짜) 를 전달받아 한 주의 총 근무시간 구해오기 
		@Override
		public String getWorkTime(Map<String, Object> workTimeMap) {
			String time = sqlsession.selectOne("emp.getWorkTime", workTimeMap);
			return time;
		}

		@Override
		public int getManagerEmpno(Map<String, Object> paraMap) {
			int manager_yn = sqlsession.selectOne("emp.getManagerEmpno",paraMap);
			return manager_yn;
		}

		@Override
		public int insertPsa(Map<String, Object> paraMap) {
			int result = sqlsession.insert("emp.insertPsa", paraMap);
			return result;
		}

		@Override
		public int updatePsa(Map<String, Object> paraMap) {
			int result = sqlsession.update("emp.updatePsa", paraMap);
			return result;
		}

		@Override
		public int updateManagerEmpno(Map<String, Object> paraMap) {
			int result = sqlsession.update("emp.updateManagerEmpno",paraMap);
			return result;
		}

		@Override
		public List<Map<String, String>> getPsaHistory(Map<String, Object> paraMap) {
			List<Map<String, String>> psaHistoryList = sqlsession.selectList("emp.getPsaHistory",paraMap);
			return psaHistoryList;
		}

		@Override
		public List<Map<String, String>> getLeaveAbsence(Map<String, Object> paraMap) {
			List<Map<String, String>> leaveAbsenceList = sqlsession.selectList("emp.getLeaveAbsence",paraMap);
			return leaveAbsenceList;
		}

		@Override
		public int changePsInfo(Map<String, Object> psInfoMap) {
			int result = sqlsession.update("emp.changePsInfo", psInfoMap);
			return result;
		}

		@Override
		public int getTotalPsaPage(Map<String, Object> pageMap) {
			int totalCount = sqlsession.selectOne("emp.getTotalPsaPage",pageMap);
			return totalCount;
		}

		@Override
		public List<Map<String, String>> psaListSearchWithPaging(Map<String, Object> pageMap) {
			List<Map<String,String>> psaListPaging = sqlsession.selectList("emp.psaListSearchWithPaging", pageMap);
			return psaListPaging;
		}

		@Override
		public void addFile(Map<String, Object> fileMap) {
			sqlsession.insert("emp.addFile", fileMap);
			
		}

		@Override
		public List<Map<String, String>> getFile(Map<String, Object> paraMap) {
			List<Map<String, String>> FileList = sqlsession.selectList("emp.getFile", paraMap);
			return FileList;
		}

		@Override
		public List<Map<String, String>> empListDownloadExcel(Map<String, Object> searchMap) {
			List<Map<String,String>> empListPaging = sqlsession.selectList("emp.empListDownloadExcel", searchMap);
			return empListPaging;
		}

		@Override
		public void insertExcel(Map<String, Object> paramMap) {
			System.out.println("paramMap"+paramMap);
			sqlsession.insert("emp.registEmpExcel",paramMap);
			
		}

		@Override
		public void changePsaMemo(Map<String, Object> paraMap) {
			sqlsession.update("emp.changePsaMemo",paraMap);
			
		}

		@Override
		public Map<String, String> getLeaveInfo(Map<String, String> leaveMap) {
			Map<String, String> leaveInfoMap = sqlsession.selectOne("emp.getLeaveInfo",leaveMap);
			return leaveInfoMap;
		}

		@Override
		public int updateLeaveDate(Map<String, String> leaveMap) {
			int result = sqlsession.update("emp.updateLeaveDate",leaveMap);
			return result;
		}

		@Override
		public int cancelLeave(Map<String, Object> paraMap) {
			int result = sqlsession.delete("emp.cancelLeave",paraMap);
			return result;
		}

		@Override
		public int updateLeaveStatus(Map<String, Object> paraMap) {
			int result = sqlsession.update("emp.updateLeaveStatus",paraMap);
			return result;
		}

		@Override
		public void updateLeavejae(Map<String, String> leaveMap) {
			sqlsession.update("emp.updateLeavejae",leaveMap);
		}

		@Override
		public void retirement(Map<String, Object> empMap) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int checkApproval(Map<String, Object> paraMap) {
			int result = sqlsession.selectOne("emp.checkApproval",paraMap);
			return result;
		}

		@Override
		public int updateRetirement(Map<String, Object> paraMap) {
			int result = sqlsession.update("emp.updateRetirement", paraMap);
//			int result = sqlsession.delete("emp.deleteEmp", paraMap);
			return result;
		}

		@Override 
		public int getManagerEmpnoRegist(Map<String, Object> paraMap) {
			int manager_yn = sqlsession.selectOne("emp.getManagerEmpnoRegist",paraMap);
			return manager_yn;
		}

		@Override
		public Map<String, String> getEmpno(Map<String, Object> paraMap) {
			Map<String, String> empnoMap = sqlsession.selectOne("emp.getEmpno", paraMap);
			return empnoMap;
		}

		@Override
		public void updateManagerEmpnoRegist(Map<String, Object> paraMap) {
			sqlsession.update("emp.updateManagerEmpnoRegist", paraMap);
			
		}

		@Override
		public List<Map<String, Object>> genderRate() {
			List<Map<String, Object>> genderRateList = sqlsession.selectList("emp.genderRate");
			return genderRateList;
		}

		@Override
		public List<Map<String, Object>> empCntDept() {
			List<Map<String, Object>> empCntList = sqlsession.selectList("emp.empCntDept");
			return empCntList;
		}

		@Override
		public Map<String, String> getAnnualLeaveCnt(Map<String,String> paraMap) {
			Map<String,String> AnnualLeaveCntMap = sqlsession.selectOne("emp.getAnnualLeaveCnt",paraMap);
			return AnnualLeaveCntMap;
		}

		@Override
		public void insertAnnualLeave(Map<String, Object> paraMap) {
			sqlsession.insert("emp.insertAnnualLeave", paraMap);
		}

		@Override
		public int getTotalCnt(Map<String, Object> pageMap) {
			int cnt = sqlsession.selectOne("emp.getTotalCnt",pageMap);
			return cnt;
		}

}
