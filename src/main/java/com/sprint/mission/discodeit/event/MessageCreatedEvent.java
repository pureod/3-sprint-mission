package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record MessageCreatedEvent(
    UUID messageId,
    User author,
    Channel channel,
    String content,
    Instant createdAt
) {

}
