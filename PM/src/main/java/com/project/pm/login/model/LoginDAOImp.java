package com.project.pm.login.model;

import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.project.pm.employee.model.EmpVO;

/**
 * 로그인 데이터 접근 객체(DAO) 구현 클래스
 * 
 * LoginDAO 인터페이스의 구현체로, MyBatis를 통해 실제 데이터베이스 작업을 수행합니다.
 * Spring의 @Repository 어노테이션을 통해 DAO 빈으로 등록되어 의존성 주입이 가능합니다.
 * 
 * 이 클래스는 다음과 같은 역할을 수행합니다:
 * - MyBatis SqlSessionTemplate을 통한 데이터베이스 접근
 * - 로그인 인증을 위한 SQL 쿼리 실행
 * - 조회 결과를 EmpVO 객체로 매핑하여 반환
 */
@Repository  // Spring의 DAO 컴포넌트로 등록
public class LoginDAOImp implements LoginDAO{
	
	/**
	 * MyBatis SQL 세션 템플릿
	 * 
	 * @Resource 어노테이션을 통해 Spring 컨테이너에서 SqlSessionTemplate 빈을 주입받습니다.
	 * 이 객체를 통해 MyBatis XML 매퍼에 정의된 SQL을 실행할 수 있습니다.
	 */
	@Resource
	private SqlSessionTemplate sqlsession; 

	/**
	 * 사용자 로그인 인증을 위한 데이터베이스 조회 구현 메서드
	 * 
	 * LoginDAO 인터페이스의 checkLogin 메서드를 구현하며,
	 * MyBatis를 통해 실제 데이터베이스에서 사용자 정보를 조회합니다.
	 * 
	 * 처리 흐름:
	 * 1. SqlSessionTemplate의 selectOne 메서드 호출
	 * 2. "emp.checkLogin" 매퍼 ID로 SQL 실행
	 * 3. loginMap 파라미터를 SQL에 전달
	 * 4. 조회 결과를 EmpVO 객체로 매핑하여 반환
	 * 
	 * @param loginMap 로그인 정보가 담긴 Map 객체
	 *                 - "email": 사용자 이메일 (로그인 ID)
	 *                 - "pwd": 사용자 비밀번호
	 * @return EmpVO 인증된 직원 정보 객체 (조회 결과 없으면 null 반환)
	 */
	@Override
	public EmpVO checkLogin(Map<String, String> loginMap) {
		// MyBatis를 통해 emp.xml 매퍼의 checkLogin 쿼리 실행
		// selectOne: 단일 결과 조회 (0개 또는 1개 레코드 반환)
		EmpVO loginuser = sqlsession.selectOne("emp.checkLogin", loginMap);
		
		// 조회 결과 반환 (성공 시 EmpVO 객체, 실패 시 null)
		return loginuser;
	}

}
