package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {

    public BinaryContentNotFoundException(UUID binaryContentId) {
        super(ErrorCode.BinaryContent_Not_Found, Map.of("binaryContentId", binaryContentId));
    }
}
