package com.sprint.mission.discodeit.controller;

import com.nimbusds.jose.JOSEException;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.exception.auth.InvalidTokenException;
import com.sprint.mission.discodeit.dto.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.jwt.JwtInformation;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final DiscodeitUserDetailsService userDetailsService;
    private final JwtRegistry jwtRegistry;
    private final JwtTokenProvider tokenProvider;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {

        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refreshToken(
        @CookieValue(
            name = JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME,
            required = false
        )
        String refreshToken,
        HttpServletResponse response
    ) {

        log.debug("[AuthController] 리프레시 토큰 재발급 요청");

        if (refreshToken == null || !jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 refreshToken입니다");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

        DiscodeitUserDetails userDetails =
            (DiscodeitUserDetails) userDetailsService.loadUserByUsername(username);

        UserDto userDto = userDetails.getUserDto();

        try {
            String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

            jwtTokenProvider.expireRefreshCookie(response);
            jwtTokenProvider.addRefreshCookie(response, newRefreshToken);

            log.debug("[AuthController] 토큰 재발급 완료 - username: {}", username);

            JwtInformation newInfo = new JwtInformation(userDto, newAccessToken, newRefreshToken);

            jwtRegistry.rotateJwtInformation(refreshToken, newInfo);
            tokenProvider.addRefreshCookie(response, newRefreshToken);

            return ResponseEntity.ok(JwtDto.of(userDto, newAccessToken));

        } catch (JOSEException e) {
            log.error("[AuthController] 토큰 생성 중 오류 발생", e);
            throw new InvalidTokenException("토큰 생성 중 오류가 발생했습니다");

        }
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> updateUserRole(
        @Valid @RequestBody UserRoleUpdateRequest userRoleUpdateRequest
    ) {
        log.debug("[AuthController] 사용자 권한 변경 요청");
        log.debug("[AuthController] 요청 데이터: {}", userRoleUpdateRequest);

        try {
            UserDto userResponse = authService.updateUserRole(userRoleUpdateRequest);
            log.debug("[AuthController] 권한 변경 성공: {}", userResponse);
            return ResponseEntity.ok(userResponse);
        } catch (IllegalArgumentException e) {
            log.debug("[AuthController] 권한 변경 실패: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
