package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 2, max = 10, message = "사용자명은 2자 이상 10자 이하로 입력해야 합니다")
    String username,

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    String email,

    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,30}$",
        message = "비밀번호는 6자 이상 30자 이하이며, 최소 하나의 영문자, 숫자, 특수문자(@$!%*?&)를 포함해야 합니다"
    )
    String password
) {

}
