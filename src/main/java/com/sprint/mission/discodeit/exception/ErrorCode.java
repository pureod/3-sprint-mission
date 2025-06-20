package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 사용자 상태
    UserStatus_Already_Exists("이미 UserStatus가 존재합니다."),
    UserStatus_Not_Found("UserStatus를 찾을 수 없습니다."),

    // 사용자
    Email_Already_Exists("이미 등록된 Email입니다."),
    UserName_Already_Exists("이미 사용 중인 UserName입니다"),
    User_Not_Found("User를 찾을 수 없습니다."),

    // 읽음 상태
    ReadStatus_Already_Exists("이미 ReadStatus가 존재합니다"),
    ReadStatus_Not_Found("ReadStatus를 찾을 수 없습니다."),

    // 메세지
    Message_Not_Found("Message를 찾을 수 없습니다."),

    // 채널
    Channel_Not_Found("Channel을 찾을 수 없습니다."),
    Private_Channel_Modification("Private Channel은 수정할 수 없습니다."),

    // 파일
    BinaryContent_Not_Found("BinaryContent를 찾을 수 없습니다."),
    FILE_PROCESSING_FAILED("이미지 처리 중 오류가 발생했습니다."),

    // 인증
    Invalid_Username_OR_Password("username 또는 password가 틀렸습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}
