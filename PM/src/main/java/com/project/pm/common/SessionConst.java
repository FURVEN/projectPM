package com.project.pm.common;

/**
 * HttpSession 속성 키 상수 모음.
 *
 * 로그인 사용자(EmpVO)는 세션에 {@link #LOGIN_USER} 키로 저장되며,
 * LoginCheckInterceptor 가 이 키의 존재 여부로 로그인 상태를 판단한다.
 * JSP 에서는 ${sessionScope.loginuser} 로 접근하므로 값 문자열을 바꾸면 안 된다.
 */
public final class SessionConst {

    /** 로그인된 사용자 정보(EmpVO)가 저장되는 세션 속성 키 */
    public static final String LOGIN_USER = "loginuser";

    private SessionConst() {
    }
}
