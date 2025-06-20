package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidUsernameOrPasswordException extends AuthException {

    public InvalidUsernameOrPasswordException(String username) {
        super(ErrorCode.Invalid_Username_OR_Password, Map.of("username", username));
    }
}
