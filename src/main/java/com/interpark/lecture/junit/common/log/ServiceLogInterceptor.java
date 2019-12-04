package com.interpark.lecture.junit.common.log;

import com.interpark.ncl.std.common.log.LogInterceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * 로그 기록을 위해서 사용자 세션 정보를 조회해서 기억하는 클래스
 */
public class ServiceLogInterceptor extends LogInterceptor {

    @Override
    protected void setSessionInfo(HttpServletRequest request) {

        // TODO : 세션에 추가로 닮고 싶은 정보
        // String userAgent = request.getHeader("user-agent");
        // request.setAttribute(LogSessionInfoType.USER_AGENT.toString(), userAgent);
    }
}