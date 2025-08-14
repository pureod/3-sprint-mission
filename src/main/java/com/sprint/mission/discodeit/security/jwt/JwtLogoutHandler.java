package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
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
    private final JwtRegistry jwtRegistry;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        log.debug("[JwtLogoutHandler] 로그아웃 처리 시작: 리프레시 쿠키 만료 응답 추가");

        tokenProvider.expireRefreshCookie(response);

        log.warn("분기 바로 시작 전");

        if (authentication != null
            && authentication.getPrincipal() instanceof DiscodeitUserDetails userDetails) {

            log.warn("분기로 인해 상태 변화가 이루어지지 않음");

            jwtRegistry.invalidateJwtInformationByUserId(userDetails.userId());
            log.debug("[JwtLogoutHandler] JWT Registry에서 사용자 정보 제거 완료 - userId: {}",
                userDetails.userId());
        }


    }
}
