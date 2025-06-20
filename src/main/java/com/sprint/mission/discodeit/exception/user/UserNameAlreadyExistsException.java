package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNameAlreadyExistsException extends UserException {

    public UserNameAlreadyExistsException(String username) {
        super(ErrorCode.UserName_Already_Exists, Map.of("username", username));
    }
}
