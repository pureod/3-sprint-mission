package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class PrivateChannelModificationException extends ChannelException {

    public PrivateChannelModificationException(UUID channelId) {
        super(ErrorCode.Private_Channel_Modification, Map.of("channelId", channelId));
    }
}
