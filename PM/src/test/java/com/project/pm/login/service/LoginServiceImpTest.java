package com.project.pm.login.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.project.pm.employee.model.EmpVO;
import com.project.pm.login.model.LoginDAO;

/**
 * LoginServiceImp 단위 테스트.
 * Oracle 없이 실행 가능하도록 LoginDAO 를 수제 페이크로 대체한다.
 */
public class LoginServiceImpTest {

    @Test
    public void checkLoginDelegatesToDaoAndReturnsMatchedUser() {
        final EmpVO expected = new EmpVO();
        expected.setEmpno("1001");
        expected.setEmail("user@test.com");

        LoginDAO fakeDao = new LoginDAO() {
            @Override
            public EmpVO checkLogin(Map<String, String> loginMap) {
                if ("user@test.com".equals(loginMap.get("email"))
                        && "correct-pw".equals(loginMap.get("pwd"))) {
                    return expected;
                }
                return null;
            }
        };

        LoginServiceImp service = new LoginServiceImp(fakeDao);

        Map<String, String> ok = new HashMap<String, String>();
        ok.put("email", "user@test.com");
        ok.put("pwd", "correct-pw");
        assertSame(expected, service.checkLogin(ok));

        Map<String, String> wrong = new HashMap<String, String>();
        wrong.put("email", "user@test.com");
        wrong.put("pwd", "bad-pw");
        assertNull(service.checkLogin(wrong));
    }
}
