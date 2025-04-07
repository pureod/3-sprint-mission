package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {


        //----------------------User----------------
        UserService userService = new JCFUserService();

        //1.등록
        System.out.println("----------사용자 등록----------");
        User person1 = userService.create("이주용", "SB001", "B001", "sb001@gmail.com");
        User person2 = userService.create("황지인", "SB002", "B002", "sb002@gmail.com");
        User person3 = userService.create("백은호", "SB003", "B003", "sb003@gmail.com");
        User person4 = userService.create("조현아", "SB004", "B004", "sb004@gmail.com");
        User person5 = userService.create("정윤지", "SB005", "B005", "sb005@gmail.com");

        System.out.println(person1.getUserName() + "님이 등록되었습니다");
        System.out.println(person2.getUserName() + "님이 등록되었습니다");
        System.out.println(person3.getUserName() + "님이 등록되었습니다");
        System.out.println(person4.getUserName() + "님이 등록되었습니다");
        System.out.println(person5.getUserName() + "님이 등록되었습니다");
        System.out.println();

        //2-1.전체 사용자 조회
        System.out.println("----------전체 사용자 조회----------");
        userService.readAll().forEach(System.out::println);
        System.out.println();

        //2-2.특정 사용자 조회
        System.out.println("----------특정 사용자 조회----------");
        System.out.println(userService.readById(person1.getId()));
        System.out.println();

        //3.사용자 정보 수정
        System.out.println("----------사용자 정보 수정----------");
        System.out.print("수정 전 사용자 정보: ");
        User targetPerson = person5;
        System.out.println(userService.readById(targetPerson.getId()));
        userService.update(targetPerson, "장주현", "SB006", "B006", "sb006@gmail.com");
        System.out.println();

        //4. 수정된 데이터 조회
        System.out.println("----------수정된 데이터 조회----------");
        System.out.print("수정 후 사용자 정보: ");
        System.out.println(userService.readById(person5.getId()));
        System.out.println();


        //5. 삭제
        System.out.println("----------데이터 삭제----------");
        System.out.print("삭제 할 사용자 정보: ");
        targetPerson = person4;
        System.out.println(userService.readById(targetPerson.getId()));
        userService.deleteById(targetPerson);
        System.out.println();


        //6. 삭제 후 조회
        System.out.println("----------삭제 후 데이터 조회----------");
        userService.readAll().forEach(System.out::println);
        System.out.println();

        //----------------------Channel----------------------
        ChannelService channelService = new JCFChannelService();

        //1.등록
        System.out.println("----------채널 등록----------");
        Channel ch1 = channelService.create("스프린트 스프링 3기", "스프링 백엔드", false, person1, "");
        Channel ch2 = channelService.create("스프린트 커뮤니티", "스프링 백엔드", false, person2, "");
        Channel ch3 = channelService.create("롤 다인큐", "스프링 백엔드", true, person3, "1234");
        Channel ch4 = channelService.create("야놀자", "스프링 백엔드", false, person4, "");
        Channel ch5 = channelService.create("점메추", "스프링 백엔드", false, person5, "");
        System.out.println(ch1.getChannelName() + " 채널이 생성 되었습니다");
        System.out.println(ch2.getChannelName() + " 채널이 생성 되었습니다");
        System.out.println(ch3.getChannelName() + " 채널이 생성 되었습니다");
        System.out.println(ch4.getChannelName() + " 채널이 생성 되었습니다");
        System.out.println(ch5.getChannelName() + " 채널이 생성 되었습니다");
        System.out.println();

        //2-1.전체 채널 조회
        System.out.println("----------전체 채널 조회----------");
        channelService.readAll().forEach(System.out::println);
        System.out.println();


        //2-2.특정 채널 조회
        System.out.println("----------특정 채널 조회----------");
        System.out.println(channelService.readById(ch1.getId()));
        System.out.println();

        //3.채널 정보 수정
        System.out.println("----------채널 정보 수정----------");
        System.out.print("수정 전 채널 정보: ");
        Channel targetChannel = ch3;
        System.out.println(channelService.readById(targetChannel.getId()));
        channelService.update(targetChannel, "롤 듀오", "영웅호걸", true);
        System.out.println();

        //4. 수정된 데이터 조회
        System.out.println("----------수정된 데이터 조회----------");
        System.out.print("수정 후 채널 정보: ");
        System.out.println(channelService.readById(targetChannel.getId()));
        System.out.println();

        //5. 삭제
        System.out.println("----------채널 삭제----------");
        System.out.print("삭제 할 채널 정보: ");
        targetChannel = ch4;
        System.out.println(channelService.readById(targetChannel.getId()));
        channelService.deleteById(person4, targetChannel);
        System.out.println();

        //6. 삭제 후 조회
        System.out.println("----------채널 삭제 후 데이터 조회----------");
        channelService.readAll().forEach(System.out::println);
        System.out.println();

        //7. 채널 멤버 추가
        System.out.println("----------채널 멤버 추가 ----------");
        channelService.joinChannel(person2, ch3, "1234");
        channelService.joinChannel(person1, ch3, "1234");
        System.out.println("총 " + ch3.getMemberCount() + "명의 사용자가 채널에 있습니다.");
        System.out.println(ch3.getMemberList());
        System.out.println();

        //8. 채널 멤버 제거
        System.out.println("----------채널 멤버 제거 ----------");
        channelService.leave(person2, ch3);
        System.out.println();

        //----------------------Message----------------------

        List<Message> messageList = new ArrayList<>();
        MessageService messageService = new JCFMessageService(userService, channelService);

        //1.등록
        System.out.println("----------메세지 전송----------");

        Message message1 = messageService.create(person1, ch1, "위워크 금욜 ㄱㄱ");
        Message message2 = messageService.create(person2, ch1, "ㅇㅋㅇㅋ");
        Message message3 = messageService.create(person3, ch3, "미드");
        Message message4 = messageService.create(person2, ch1, "예약 완료");
        Message message5 = messageService.create(person5, ch1, "ㅇㅋ");
        Message message6 = messageService.create(person1, ch3, "정글");

        System.out.println(message1.getChannel().getChannelName() + " 채널에 " + message1.getUser().getUserName() + "님이 메세지를 보냈습니다.");
        System.out.println(message2.getChannel().getChannelName() + " 채널에 " + message2.getUser().getUserName() + "님이 메세지를 보냈습니다.");
        System.out.println(message3.getChannel().getChannelName() + " 채널에 " + message3.getUser().getUserName() + "님이 메세지를 보냈습니다.");
        System.out.println(message4.getChannel().getChannelName() + " 채널에 " + message4.getUser().getUserName() + "님이 메세지를 보냈습니다.");
        System.out.println(message5.getChannel().getChannelName() + " 채널에 " + message5.getUser().getUserName() + "님이 메세지를 보냈습니다.");
        System.out.println(message6.getChannel().getChannelName() + " 채널에 " + message6.getUser().getUserName() + "님이 메세지를 보냈습니다.");

        System.out.println();

        //2-1.전체 메세지 조회
        System.out.println("----------전체 메세지 조회----------");
        messageService.readAll().forEach(System.out::println);
        System.out.println();


        //2-2.특정 채널의 메세지 조회
        System.out.println("----------특정 채널 메세지 조회----------");
        messageService.readByChannelId(ch3.getId()).forEach(System.out::println);
        System.out.println();

        //3.메세지 정보 수정
        System.out.println("----------메세지 내용 수정----------");
        System.out.print("수정 전 메세지 내용: ");
        System.out.println(messageService.readById(message6.getId()));
        messageService.update(message6.getId(), "아니다 탑 감");
        System.out.println();

        //4. 수정된 데이터 조회
        System.out.println("----------수정된 내용 조회----------");
        System.out.print("수정 후 메세지 내용: ");
        System.out.println(messageService.readById(message6.getId()));
        System.out.println();

        //5. 삭제
        System.out.println("----------메세지 삭제----------");
        System.out.print("삭제 할 메세지 정보: ");
        System.out.println(messageService.readById(message4.getId()));
        messageService.deleteById(message4.getId());
        System.out.println();


        //6. 삭제 후 조회
        System.out.println("----------메세지 삭제 후 데이터 조회----------");
        messageService.readByChannelId(ch3.getId()).forEach(System.out::println);
        System.out.println();

//        7. 유효성 검사 (현재 person4와 ch4 삭제 테스트로 인해 삭제된 상태)
//        messageService.create(person4,ch1,"삭제된 유저");
//        messageService.create(person1,ch4,"삭제된 채널");
//        messageService.create(person1,ch3,"장실 점");

    }
}
