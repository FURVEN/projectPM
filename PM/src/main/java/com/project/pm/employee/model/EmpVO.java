package com.project.pm.employee.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 직원(Employee) 정보를 담는 Value Object (VO) 클래스
 * 
 * 이 클래스는 프로젝트 관리 시스템의 핵심 도메인 객체로서, 직원의 모든 정보를 담고 있습니다.
 * Lombok 어노테이션을 활용하여 Getter, Setter, toString 메서드를 자동 생성합니다.
 * 
 * 주요 용도:
 * - 데이터베이스의 직원 테이블과 매핑
 * - 로그인 사용자 세션 정보 저장
 * - 직원 관리 기능에서 데이터 전송 객체(DTO) 역할
 * - MyBatis 결과 매핑 객체
 */
@Setter    // Lombok: 모든 필드의 Setter 메서드 자동 생성
@Getter    // Lombok: 모든 필드의 Getter 메서드 자동 생성
@ToString  // Lombok: toString() 메서드 자동 생성 (디버깅 시 유용)
public class EmpVO {
	
	// ========== 기본 직원 정보 ==========
	
	/**
	 * 사원번호 (Primary Key)
	 * 직원을 고유하게 식별하는 번호
	 */
	private String empno;
	
	/**
	 * 직원 성명 (한글)
	 * 직원의 한글 이름
	 */
	private String name;
	
	/**
	 * 영문 성 (Last Name)
	 * 직원의 영문 성씨
	 */
	private String lastname;
	
	/**
	 * 영문 이름 (First Name)
	 * 직원의 영문 이름
	 */
	private String firstname;
	
	// ========== 로그인 및 연락처 정보 ==========
	
	/**
	 * 이메일 (로그인 ID)
	 * 시스템 로그인 시 사용되는 이메일 주소
	 */
	private String email;
	
	/**
	 * 비밀번호
	 * 로그인 인증에 사용되는 비밀번호 (암호화 저장 권장)
	 */
	private String pwd;
	
	/**
	 * 휴대폰 번호
	 * 직원의 연락 가능한 휴대폰 번호
	 */
	private String mobile;
	
	// ========== 근무 관련 정보 ==========
	
	/**
	 * 입사일
	 * 직원이 회사에 입사한 날짜 (YYYY-MM-DD 형식)
	 */
	private String hiredate;
	
	/**
	 * 퇴사일
	 * 직원이 회사에서 퇴사한 날짜 (재직 중인 경우 null 또는 빈 값)
	 */
	private String retiredate;
	
	/**
	 * 부서번호 (Foreign Key)
	 * 직원이 소속된 부서의 고유 번호
	 */
	private String fk_deptno;
	
	/**
	 * 시급
	 * 직원의 시간당 급여 정보
	 */
	private String time_salary;
	
	/**
	 * 직위/직책
	 * 직원의 조직 내 직위 (예: 사원, 대리, 과장, 부장 등)
	 */
	private String position;
	
	/**
	 * 재직 상태
	 * 직원의 현재 재직 여부를 나타내는 컬럼 (Y: 재직, N: 퇴직)
	 */
	private String status;
	
	// ========== 개인 정보 ==========
	
	/**
	 * 주민등록번호
	 * 직원의 주민등록번호 (개인정보 보호 주의)
	 */
	private String rrn;
	
	/**
	 * 주소
	 * 직원의 거주지 주소
	 */
	private String address;
	
	/**
	 * 급여 지급 계좌
	 * 급여가 입금될 은행 계좌 정보
	 */
	private String account;
	
	/**
	 * 자기소개
	 * 직원의 자기소개 또는 프로필 정보
	 */
	private String introduce;
	
	/**
	 * 프로필 색상
	 * UI에서 직원을 구분하기 위한 색상 코드
	 */
	private String profile_color;
	
	// ========== 조인 및 계산 필드 ==========
	
	/**
	 * 성별
	 * 주민등록번호를 기반으로 Oracle 함수 func_gender()를 통해 계산된 성별
	 * 데이터베이스에서 동적으로 생성되는 필드
	 */
	private String gender;
	
	/**
	 * 부서명
	 * fk_deptno와 조인하여 가져온 부서의 실제 이름
	 * 데이터베이스 조인을 통해 설정되는 필드
	 */
	private String deptname;

}
