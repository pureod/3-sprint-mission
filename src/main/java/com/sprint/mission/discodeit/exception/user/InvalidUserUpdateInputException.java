package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidUserUpdateInputException extends UserException {

    public InvalidUserUpdateInputException(String newUsername) {
        super(ErrorCode.Invalid_User_Update_Input, Map.of("newUsername", newUsername));
    }
}
