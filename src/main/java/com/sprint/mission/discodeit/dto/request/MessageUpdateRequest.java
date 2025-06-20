package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

public record MessageUpdateRequest(
    @NotNull(message = "내용이 null값 일 수는 없습니다")
    String newContent
) {

}
