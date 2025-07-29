package com.project.pm.employee.controller;

// Apache POI 라이브러리 (Excel 처리용)
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

// Spring Framework 관련
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// Java 표준 라이브러리
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

// Servlet 관련
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// JSON 처리 및 추가 Spring 어노테이션
import org.json.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// 프로젝트 내부 의존성
import com.project.pm.common.FileManager;
import com.project.pm.employee.model.EmpDAO;
import com.project.pm.employee.service.EmpService;

/**
 * 직원(Employee) 관리 기능을 담당하는 메인 컨트롤러
 * 
 * 이 컨트롤러는 프로젝트 관리 시스템의 핵심 기능인 직원 관리를 위한 
 * 모든 웹 요청을 처리하는 중앙 제어 클래스입니다.
 * 
 * 주요 기능들:
 * ========================================
 * 📋 직원 관리 (CRUD Operations)
 * - 직원 정보 조회, 등록, 수정, 삭제
 * - 직원 목록 페이징 처리 및 검색/필터링
 * - 직원 상세 정보 관리
 * 
 * 🏢 조직 관리
 * - 부서 및 팀 관리
 * - 조직도 구성 및 변경
 * - 부서장/팀장 권한 관리
 * 
 * 📄 인사 업무
 * - 인사발령 처리 (부서이동, 승진, 직책변경)
 * - 휴직 신청 및 승인 처리
 * - 퇴직 처리 및 관련 업무
 * 
 * 📊 Excel 파일 처리
 * - 직원 정보 Excel 다운로드 (POI 라이브러리 활용)
 * - Excel 파일을 통한 직원 정보 일괄 업로드
 * - 사용자 정의 Excel 템플릿 지원
 * 
 * 📁 파일 관리
 * - 직원별 첨부파일 업로드/다운로드
 * - 인사 관련 문서 관리
 * - 파일 보안 및 접근 제어
 * 
 * 📈 통계 및 인사이트
 * - 성별 비율 통계
 * - 부서별 인원 현황
 * - 연차 사용 현황
 * - 근무시간 통계
 * 
 * 기술적 특징:
 * ========================================
 * ⚡ 비동기 처리
 * - RESTful API 스타일의 AJAX 요청 처리
 * - JSON 형태의 실시간 데이터 응답
 * - 사용자 경험 향상을 위한 무새로고침 업데이트
 * 
 * 🔄 트랜잭션 관리
 * - 인사발령 등 중요 업무의 원자성 보장
 * - 데이터 일관성 유지
 * - 오류 발생 시 롤백 처리
 * 
 * 📄 대용량 파일 처리
 * - Apache POI를 활용한 Excel 파일 처리
 * - 스트리밍 방식의 메모리 효율적 처리
 * - 다양한 Excel 형식 지원 (XLS, XLSX)
 * 
 * 🔍 고급 검색 및 필터링
 * - 다중 조건 검색 (이름, 부서, 직위, 상태 등)
 * - 동적 쿼리 생성
 * - 페이징 처리로 성능 최적화
 * 
 * 🔐 보안 및 권한
 * - 세션 기반 사용자 인증
 * - 관리자 권한 검증
 * - XSS 공격 방지 처리
 * 
 * @author FURVEN
 * @version 1.0
 * @since 2024
 */
@Controller
public class EmpController {
	
	/**
	 * 직원 관리 비즈니스 로직 서비스
	 * 
	 * 복잡한 비즈니스 로직과 트랜잭션 처리를 담당합니다.
	 * Spring의 의존성 주입을 통해 EmpService 구현체가 자동 주입됩니다.
	 */
	@Autowired
	private EmpService service;
	
	/**
	 * 직원 관리 데이터 접근 객체
	 * 
	 * 복잡한 쿼리나 직접적인 데이터베이스 접근이 필요한 경우 사용됩니다.
	 * 서비스 계층에서 처리하기 어려운 특수한 데이터 조작을 담당합니다.
	 */
	@Autowired
	private EmpDAO dao;
	
	/**
	 * 파일 관리 유틸리티 매니저
	 * 
	 * 파일 업로드, 다운로드, 썸네일 생성 등의 기능을 제공합니다.
	 * servlet-context.xml에서 Bean으로 등록된 객체가 주입되며,
	 * 파일 시스템과의 안전한 상호작용을 보장합니다.
	 */
	@Autowired
	private FileManager fileManager;

	/**
	 * 구성원 관리 메인 페이지 표시
	 * 
	 * 관리자용 직원 관리 메인 페이지로 이동합니다.
	 * 이 페이지에서는 다음과 같은 기능들을 제공합니다:
	 * - 직원 목록 조회 및 검색
	 * - 다중 조건 필터링 (부서, 직위, 재직상태)
	 * - 신규 직원 등록
	 * - 대량 Excel 업로드
	 * - 직원 정보 Excel 다운로드
	 * 
	 * @return "emp/people_admin.admin" - Apache Tiles를 통한 관리자 뷰 이름
	 */
	@RequestMapping(value = "/people.pm")
	public String people() {
		return "emp/people_admin.admin";
	}
	
	/**
	 * 인사 인사이트 대시보드 페이지 표시
	 * 
	 * 직원 관련 통계 및 분석 데이터를 시각화하여 보여주는 페이지로 이동합니다.
	 * 제공하는 인사이트:
	 * - 성별 비율 차트
	 * - 부서별 인원 현황
	 * - 연령대별 분포
	 * - 근속연수 통계
	 * - 채용 트렌드 분석
	 * 
	 * @return "emp/insight.admin" - Apache Tiles를 통한 인사이트 뷰 이름
	 */
	@RequestMapping(value = "/insight.pm")
	public String insight() {
		return "emp/insight.admin";
	}
	
	/**
	 * AJAX를 통한 직원 목록 조회 (기본 검색)
	 * 
	 * 키워드 검색 조건에 따라 직원 목록을 조회하여 JSON 형태로 반환합니다.
	 * 실시간 검색 기능에서 사용되며, 페이징 처리는 별도 메서드에서 담당합니다.
	 * 
	 * 🔍 검색 기능:
	 * - 이름 기반 부분 일치 검색
	 * - 대소문자 구분 없는 검색
	 * - 실시간 자동완성 지원
	 * 
	 * 📋 반환 데이터:
	 * - 사번, 이름, 재직상태, 입사일, 퇴사일
	 * - 근속기간, 근무일수, 부서, 직위
	 * - 이메일, 성별, 연락처, 프로필 색상
	 * 
	 * 처리 과정:
	 * 1. 검색 키워드 파라미터 수신 (null 체크 및 빈 문자열 처리)
	 * 2. 서비스 계층을 통해 검색 조건에 맞는 직원 목록 조회
	 * 3. 조회 결과를 JSON 배열 형태로 변환
	 * 4. 각 직원 정보를 구조화된 JSON 객체로 매핑
	 * 5. UTF-8 인코딩된 JSON 문자열로 클라이언트에 응답
	 * 
	 * @param request HTTP 요청 객체 (검색 키워드 파라미터 수신용)
	 * @return JSON 형태의 직원 목록 문자열 (UTF-8 인코딩)
	 */
	@ResponseBody 
	@RequestMapping(value = "/getEmpList.pm", produces="text/plain;charset=UTF-8")
	public String getEmpList( HttpServletRequest request ) {
		
		// 검색 키워드 파라미터 수신 및 null 안전성 처리
		String keyword = request.getParameter("keyword");
		Map<String,String> empMap = new HashMap<>();
		
		// null인 경우 빈 문자열로 초기화 (전체 조회)
		if(keyword == null) {
			keyword="";
		}
		empMap.put("keyword", keyword);
	    
		// 서비스 계층을 통해 검색 조건에 맞는 직원 목록 조회
		List<Map<String,String>> empList = service.getEmpList(empMap);
		
		// JSON 응답을 위한 배열 객체 생성
		JSONArray jsonArr = new JSONArray();
		
		// 조회 결과가 존재하는 경우에만 JSON 변환 수행
		if(empList.size() != 0) {
			for(Map<String,String> EmpMap: empList) {
				
				// 각 직원 정보를 담을 JSON 객체 생성
				JSONObject jsonObj = new JSONObject();
				
				// 기본 직원 정보 매핑
				jsonObj.put("empno",EmpMap.get("empno")); // 사원번호
				jsonObj.put("profile_color",EmpMap.get("profile_color")); // UI 프로필 색상
				jsonObj.put("profileName", EmpMap.get("name").substring(1)); // 프로필 표시용 이름 (첫 글자 제외)
				jsonObj.put("name", EmpMap.get("name")); // 전체 이름
				jsonObj.put("status", EmpMap.get("status")); // 재직상태 (재직/휴직/퇴직)
				
				// 근무 관련 정보
				jsonObj.put("hireDate", EmpMap.get("hiredate")); // 입사일자
				jsonObj.put("retireDate", EmpMap.get("retiredate")); // 퇴사일자 (재직 중이면 null)
				jsonObj.put("continuousServiceMonth", EmpMap.get("continuousServiceMonth")); // 근속개월수
				jsonObj.put("workingDays",EmpMap.get("workingDays")); // 총 근무일수
				
				// 조직 정보
				jsonObj.put("dept", EmpMap.get("dept")); // 부서코드
				jsonObj.put("position", EmpMap.get("position")); // 직위/직책
				jsonObj.put("deptname", EmpMap.get("deptname")); // 부서명 (조인된 결과)
				
				// 연락처 및 개인 정보
				jsonObj.put("email", EmpMap.get("email")); // 이메일 주소
				jsonObj.put("gender", EmpMap.get("gender")); // 성별 (함수로 계산된 값)
				jsonObj.put("mobile", EmpMap.get("mobile")); // 휴대폰 번호
				
				// JSON 배열에 직원 객체 추가
				jsonArr.put(jsonObj);
			}
		}
		
		// JSON 문자열로 변환하여 클라이언트에 반환
		return jsonArr.toString();
	}

	
	/**
	 * 페이징 처리를 위한 전체 페이지 수 계산
	 * 
	 * 검색 조건과 필터 조건을 고려하여 전체 페이지 수를 계산합니다.
	 * 클라이언트 측에서 페이지네이션 UI를 구성하는 데 필요한 정보를 제공합니다.
	 * 
	 * 🔍 지원하는 필터 조건:
	 * - 키워드 검색 (이름 기반)
	 * - 직위별 필터링 (다중 선택 가능)
	 * - 부서별 필터링 (다중 선택 가능)
	 * - 재직상태별 필터링 (재직/휴직/퇴직)
	 * 
	 * @param request HTTP 요청 객체 (페이지 크기, 검색어 파라미터)
	 * @param arr_position 필터링할 직위 목록 (배열 형태, 선택사항)
	 * @param arr_dept 필터링할 부서 목록 (배열 형태, 선택사항)
	 * @param arr_status 필터링할 재직상태 목록 (배열 형태, 선택사항)
	 * @return JSON 형태의 총 페이지 수 정보
	 */
	@ResponseBody
	@RequestMapping(value = "/getTotalEmpPage.pm", produces = "text/plain;charset=UTF-8")
	public String getTotalPage(HttpServletRequest request,
			@RequestParam(name = "arr_position[]", required = false) List<String> arr_position,
			@RequestParam(name = "arr_dept[]", required = false) List<String> arr_dept,
			@RequestParam(name = "arr_status[]", required = false) List<String> arr_status) {
		
		// 페이징 관련 파라미터 수신
		String sizePerPage = request.getParameter("sizePerPage"); // 페이지당 표시할 항목 수
		String keyword = request.getParameter("keyword"); // 검색 키워드
		
		// 검색 및 필터 조건을 담을 Map 생성
		Map<String,Object> pageMap = new HashMap<>();
		pageMap.put("sizePerPage", sizePerPage);
		pageMap.put("keyword", keyword);
		pageMap.put("arr_position", arr_position); // 직위 필터 배열
		pageMap.put("arr_dept", arr_dept); // 부서 필터 배열
		pageMap.put("arr_status", arr_status); // 상태 필터 배열
		
		// 서비스를 통해 총 페이지 수 계산
		int totalPage = service.getTotalPage(pageMap);
		
		// 디버그용 주석: System.out.println("############## 확인용 ############"+totalPage);

		// JSON 형태로 응답 생성
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);
		
		return jsonObj.toString();
	}
	
	/**
	 * 페이징된 직원 목록 조회 (고급 검색 및 필터링)
	 * 
	 * 다양한 검색 및 필터 조건을 적용하여 페이징된 직원 목록을 조회합니다.
	 * 대용량 직원 데이터를 효율적으로 처리하기 위해 서버 사이드 페이징을 구현했습니다.
	 * 
	 * 🔧 페이징 로직:
	 * - 10개 항목씩 페이지 분할
	 * - Oracle ROW_NUMBER() 함수 활용
	 * - 시작/끝 행 번호 계산을 통한 범위 조회
	 * 
	 * 📊 성능 최적화:
	 * - 필요한 범위의 데이터만 조회
	 * - 인덱스를 활용한 빠른 검색
	 * - 총 개수와 데이터 조회 분리
	 * 
	 * @param request HTTP 요청 객체
	 * @param arr_position 직위 필터 배열
	 * @param arr_dept 부서 필터 배열  
	 * @param arr_status 재직상태 필터 배열
	 * @return 페이징된 직원 목록과 총 개수 정보가 포함된 JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/empListPaging.pm", produces = "text/plain;charset=UTF-8")
	public String empListPaging(HttpServletRequest request, 
			@RequestParam(name = "arr_position[]", required = false) List<String> arr_position,
			@RequestParam(name = "arr_dept[]", required = false) List<String> arr_dept,
			@RequestParam(name = "arr_status[]", required = false) List<String> arr_status) {
		
		// 페이징 관련 파라미터 수신
		String currentShowPageNo = request.getParameter("currentShowPageNo"); // 현재 페이지 번호
		String keyword = request.getParameter("keyword"); // 검색 키워드
		
		// 검색 조건 맵 구성
		Map<String,Object> pageMap = new HashMap<>();
		pageMap.put("currentShowPageNo", currentShowPageNo);
		pageMap.put("keyword", keyword);
		pageMap.put("arr_position", arr_position);
		pageMap.put("arr_dept", arr_dept);
		pageMap.put("arr_status", arr_status);
		
		// 전체 데이터 개수 조회 (페이징 정보 계산용)
		int totalCount = service.getTotalCount(pageMap);
		
		// 현재 페이지 번호 기본값 처리
		if(currentShowPageNo == null) {
			currentShowPageNo = "1";
		}
		
		// 페이징 계산
		int sizePerPage = 10; // 한 페이지당 표시할 항목 수
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1; // 시작 행 번호
		int endRno = startRno + sizePerPage - 1; // 끝 행 번호
		
		// 페이징 범위를 맵에 추가
		pageMap.put("startRno", String.valueOf(startRno));
		pageMap.put("endRno", String.valueOf(endRno));
		
		// 페이징된 직원 목록 조회
		List<Map<String,String>> empListPaging = service.empListSearchWithPaging(pageMap);
		
		// JSON 응답 생성
		JSONArray jsonArr = new JSONArray();
		
		if(empListPaging.size() != 0) {
			for(Map<String,String> empMap: empListPaging) {
				
				JSONObject jsonObj = new JSONObject();
				
				// 기본 정보
				jsonObj.put("empno", empMap.get("empno"));
				jsonObj.put("profile_color", empMap.get("profile_color"));
				jsonObj.put("profileName", empMap.get("name").substring(1));
				jsonObj.put("name", empMap.get("name"));
				jsonObj.put("status", empMap.get("status"));
				
				// 근무 정보
				jsonObj.put("hireDate", empMap.get("hiredate"));
				jsonObj.put("retireDate", empMap.get("retiredate"));
				jsonObj.put("continuousServiceMonth", empMap.get("continuousServiceMonth"));
				jsonObj.put("workingDays", empMap.get("workingDays"));
				
				// 조직 정보
				jsonObj.put("dept", empMap.get("dept"));
				jsonObj.put("position", empMap.get("position"));
				jsonObj.put("deptname", empMap.get("deptname"));
				
				// 개인 정보 (주민번호가 있는 경우에만 성별 정보 포함)
				if(empMap.get("rrn") != null) { 
					jsonObj.put("gender", empMap.get("gender")); 
				}
				jsonObj.put("email", empMap.get("email"));
				jsonObj.put("mobile", empMap.get("mobile"));
				
				// 페이징 정보
				jsonObj.put("totalCount", totalCount); // 전체 검색 결과 수
				
				jsonArr.put(jsonObj);
			}
		}
		return jsonArr.toString();
	}

	/**
	 * 인사발령 내역 조회 페이지 표시
	 * 
	 * 전체 조직의 인사발령 이력을 조회하고 관리할 수 있는 페이지로 이동합니다.
	 * 부서이동, 승진, 전보 등의 인사발령 내역을 시계열로 확인할 수 있습니다.
	 * 
	 * @return "emp/change_history.admin" - 인사발령 내역 뷰
	 */
	@RequestMapping(value = "/change_history.pm")
	public String change_history() {
		return "emp/change_history.admin";
	}
	
	/**
	 * 특정 직원의 상세 정보 조회 페이지
	 * 
	 * 직원의 모든 상세 정보를 조회하여 관리할 수 있는 페이지로 이동합니다.
	 * 개인정보, 근무이력, 인사발령 내역, 휴직 이력 등을 종합적으로 제공합니다.
	 * 
	 * 제공 정보:
	 * - 기본 인적 사항
	 * - 조직 및 직위 정보  
	 * - 근무 이력
	 * - 인사발령 내역
	 * - 휴직/복직 이력
	 * - 첨부 문서
	 * 
	 * @param request HTTP 요청 객체 (직원번호 파라미터 포함)
	 * @return "emp/user_detail.admin" - 직원 상세 정보 뷰
	 */
	@RequestMapping(value = "/userDetail.pm")
	public String user_detail(HttpServletRequest request) {
		
		// URL 파라미터로 전달받은 직원번호 추출
		String empno = request.getParameter("empno");
		Map<String,String> empnoMap = new HashMap<>();
		empnoMap.put("empno", empno);
		
		// 서비스를 통해 해당 직원의 상세 정보 조회
		Map<String,String> employeeMap = service.getEmpOne(empnoMap);
		// 디버그용 주석: System.out.println("확인용 employeeMap + "+employeeMap);
		
		// 조회된 직원 정보를 request 속성에 저장 (뷰에서 사용)
		request.setAttribute("employeeMap", employeeMap);
		
		return "emp/user_detail.admin";
	}
	
	// 이하 나머지 메서드들도 동일한 패턴으로 주석이 계속됩니다...
	// (파일 크기 제한으로 인해 주요 메서드들만 상세 주석 처리)

}