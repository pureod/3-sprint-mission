package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        log.debug("[JwtLogoutHandler] 로그아웃 처리 시작: 리프레시 쿠키 만료 응답 추가");

        tokenProvider.expireRefreshCookie(response);

    }
}
