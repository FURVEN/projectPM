package com.project.pm.employee.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EmpVO {
	
	private String empno; // 사원번호
	private String name; // 이름
	private String lastname; // 영문이름
	private String firstname; // 영문 성
	private String email; // 이메일(아이디)
	private String pwd; // 비밀번호
	private String mobile; // 핸드폰번호
	private String hiredate; // 입사일
	private String retiredate; // 퇴사일
	private String fk_deptno; // 부서번호
	private String time_salary; // 급여(시급)
	private String rrn; // 주민번호
	private String position; // 직위
	private String address; // 주소
	private String account; // 급여지급 계좌
	private String introduce; // 자기소개
	private String status; // 재직여부를 나타내는 컬럼
	private String profile_color; // 프로필 색상
	
	private String gender; // 성별 (오라클에서 func_gender()로 구해옴)
	private String deptname; // 부서명

}
