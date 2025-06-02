package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    if (user == null) {
      return null;
    }

    // db 구조 상 null 값을 불허하지만 추후 확장성을 고려하여 추가
    Boolean online = user.getStatus() != null ? user.getStatus().isOnline() : null;

    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(binaryContentMapper.toDto(user.getProfile()))
        .online(online)
        .build();

  }
}
