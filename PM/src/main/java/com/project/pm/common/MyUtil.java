package com.project.pm.common;

import javax.servlet.http.HttpServletRequest;

/**
 * 프로젝트 전반에서 사용되는 공통 유틸리티 클래스
 * 
 * 이 클래스는 정적 메서드들을 통해 자주 사용되는 유틸리티 기능들을 제공합니다.
 * 주로 URL 처리와 보안 관련 기능들을 포함하고 있습니다.
 */
public class MyUtil {

	/**
	 * 쿼리 파라미터를 포함한 현재 URL 주소를 반환하는 메서드
	 * 
	 * HTTP 요청에서 현재 접속한 URL의 컨텍스트 패스 이후 부분을 추출합니다.
	 * GET 방식의 쿼리 파라미터도 함께 포함하여 반환합니다.
	 * 
	 * 예시:
	 * - 전체 URL: http://localhost:9090/MyMVC/member/memberList.up?sizePerPage=10&currentShowPageNo=5
	 * - 반환값: /member/memberList.up?sizePerPage=10&currentShowPageNo=5
	 * 
	 * 처리 과정:
	 * 1. request.getRequestURL()로 기본 URL 획득
	 * 2. request.getQueryString()로 쿼리 파라미터 획득
	 * 3. GET 방식인 경우 쿼리 파라미터를 URL에 추가
	 * 4. 컨텍스트 패스를 제거하여 상대 경로로 변환
	 * 
	 * @param request HTTP 요청 객체
	 * @return 컨텍스트 패스 이후의 상대 URL (쿼리 파라미터 포함)
	 */
	public static String getCurrentURL(HttpServletRequest request) {
		
		// 기본 요청 URL 획득
		// 예: http://localhost:9090/MyMVC/member/memberList.up
		String currentURL = request.getRequestURL().toString();
		
		// 쿼리 스트링 획득 (GET 방식인 경우에만 존재)
		// 예: sizePerPage=10&currentShowPageNo=5&searchType=name&searchWord=정
		// POST 방식인 경우 null 반환
		String queryString = request.getQueryString();
		
		// GET 방식이면서 쿼리 파라미터가 있는 경우 URL에 추가
		if(queryString != null) {
			currentURL += "?" + queryString;
		}
		
		// 컨텍스트 패스 획득 (예: /MyMVC)
		String ctxPath = request.getContextPath();
		
		// 컨텍스트 패스 이후 부분의 시작 인덱스 계산
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();
		
		// 컨텍스트 패스를 제거한 상대 URL 추출
		// 예: /member/memberList.up?sizePerPage=10&currentShowPageNo=5
		currentURL = currentURL.substring(beginIndex);
		
		return currentURL;
	}
	
	
	/**
	 * 크로스 사이트 스크립트(XSS) 공격에 대응하는 보안 코드 처리 메서드
	 * 
	 * 사용자 입력에서 악성 스크립트를 무력화하여 XSS 공격을 방지합니다.
	 * 스마트에디터 사용 여부에 따라 다른 처리 방식을 적용합니다.
	 * 
	 * 처리 방식:
	 * - 스마트에디터 미사용 시: 모든 HTML 태그를 문자 엔티티로 변환 (주석 처리됨)
	 * - 스마트에디터 사용 시: <script 태그만 선별적으로 무력화
	 * 
	 * @param str 보안 처리할 문자열 (사용자 입력값)
	 * @return XSS 공격이 무력화된 안전한 문자열
	 */
	public static String secureCode(String str) {
	
		/*
		=== 스마트에디터를 사용하지 않을 경우 ===
		모든 HTML 태그를 문자 엔티티로 변환하여 완전히 무력화
		
		str = str.replaceAll("<", "&lt;");  // < 기호를 HTML 엔티티로 변환
		str = str.replaceAll(">", "&gt;");  // > 기호를 HTML 엔티티로 변환
		*/	
		
		// === 스마트에디터를 사용할 경우 ===
		// HTML 태그는 허용하되, 위험한 <script 태그만 선별적으로 무력화
		// 스마트에디터에서 생성한 정상적인 HTML은 유지하면서 보안 강화
		str = str.replaceAll("<script", "&lt;script");
		
		return str;
	}
	
} // end of class MyUtil