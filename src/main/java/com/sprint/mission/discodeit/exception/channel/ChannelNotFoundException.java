package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {

    public ChannelNotFoundException(UUID channelId) {
        super(ErrorCode.Channel_Not_Found, Map.of("channelId", channelId));
    }
}
