package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getCurrentUserInfo(UserDetails userDetails) {

        log.debug("[AuthService] 현재 사용자 정보 조회 요청");

        if (userDetails == null) {
            log.debug("[AuthService] UserDetails가 null 입니다.");
            return null;
        }

        String username = userDetails.getUsername();
        log.debug("[AuthService] username: {}", username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        log.debug("[AuthService] user: {}", user);

        UserDto userResponse = userMapper.toDto(user);

        log.debug("[AuthService] userDto: {}", userResponse);

        return userResponse;
    }
}
