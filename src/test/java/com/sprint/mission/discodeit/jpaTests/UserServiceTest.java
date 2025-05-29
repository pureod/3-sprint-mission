package com.sprint.mission.discodeit.jpaTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Test
  void userCreateTest() {
    // given
    UserCreateRequest request = new UserCreateRequest("홍길동", "hong@example.com", "1234");

    // when
    User response = userService.create(request, Optional.empty());

    // then
    assertNotNull(response.getId());
    assertEquals("홍길동", response.getUsername());
    assertEquals("hong@example.com", response.getEmail());

  }

}
