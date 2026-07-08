package com.project.pm.login.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.project.pm.common.SessionConst;
import com.project.pm.employee.model.EmpVO;
import com.project.pm.login.service.LoginService;

/**
 * LoginController 단위 테스트 (스프링 컨테이너/Oracle 불필요).
 *
 * 회귀 보호 대상:
 *  - /loginContinue.pm 의 응답 형태 {"result": boolean} 유지 (login.jsp AJAX 계약)
 *  - 인증 성공 시에만 세션에 loginuser 저장
 *  - 빈 파라미터는 DB 조회 없이 실패 처리
 *  - 로그아웃 시 세션 무효화
 */
public class LoginControllerTest {

    private EmpVO knownUser;
    private boolean serviceCalled;
    private LoginController controller;

    @Before
    public void setUp() {
        knownUser = new EmpVO();
        knownUser.setEmpno("1001");
        knownUser.setEmail("user@test.com");

        serviceCalled = false;

        LoginService fakeService = new LoginService() {
            @Override
            public EmpVO checkLogin(Map<String, String> loginMap) {
                serviceCalled = true;
                if ("user@test.com".equals(loginMap.get("email"))
                        && "correct-pw".equals(loginMap.get("pwd"))) {
                    return knownUser;
                }
                return null;
            }
        };

        controller = new LoginController(fakeService);
    }

    @Test
    public void successfulLoginReturnsTrueAndStoresUserInSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Map<String, Object> response =
                controller.loginContinue("user@test.com", "correct-pw", request);

        assertEquals(Boolean.TRUE, response.get("result"));
        assertSame(knownUser,
                request.getSession().getAttribute(SessionConst.LOGIN_USER));
    }

    @Test
    public void wrongPasswordReturnsFalseAndLeavesSessionEmpty() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Map<String, Object> response =
                controller.loginContinue("user@test.com", "bad-pw", request);

        assertEquals(Boolean.FALSE, response.get("result"));
        assertNull(request.getSession().getAttribute(SessionConst.LOGIN_USER));
    }

    @Test
    public void blankParametersFailFastWithoutServiceCall() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Map<String, Object> response = controller.loginContinue(null, "  ", request);

        assertEquals(Boolean.FALSE, response.get("result"));
        assertFalse("빈 파라미터는 DAO/DB 조회 없이 거부되어야 한다", serviceCalled);
    }

    @Test
    public void logoutInvalidatesExistingSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_USER, knownUser);
        request.setSession(session);

        String view = controller.logout(request);

        assertEquals("redirect:/login.pm", view);
        assertTrue(session.isInvalid());
    }

    @Test
    public void mainRedirectsByLoginState() {
        // 비로그인 → 로그인 페이지
        MockHttpServletRequest anonymous = new MockHttpServletRequest();
        assertEquals("redirect:login.pm", controller.main(anonymous));

        // 로그인 → 공지사항 목록
        MockHttpServletRequest loggedIn = new MockHttpServletRequest();
        loggedIn.getSession().setAttribute(SessionConst.LOGIN_USER, knownUser);
        assertEquals("redirect:/notice/noticeList.pm", controller.main(loggedIn));
    }
}
