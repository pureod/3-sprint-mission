package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(
            path = "/createPublic"
//            , method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity<Channel> createPublicChannel(
            @RequestBody PublicChannelCreateRequest request
    ) {
        Channel createdChannel = channelService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @RequestMapping(
            path = "/createPrivate"
//            , method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity<Channel> createPrivateChannel(
            @RequestBody PrivateChannelCreateRequest request
    ) {
        Channel createdChannel = channelService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @RequestMapping(
            path = "/update"
//            , method = RequestMethod.PUT
    )
    @ResponseBody
    public ResponseEntity<Channel> updateChannel(
            @RequestParam("channelId") UUID channelId,
            @RequestBody PublicChannelUpdateRequest request
    ) {

        Channel updatedChannel = channelService.update(channelId, request);

        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);

    }

    @RequestMapping(
            path = "/delete"
//            , method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseEntity<String> deleteChannel(
            @RequestParam("channelId") UUID channelId
    ) {

        channelService.delete(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("[From. Server] 채널 삭제 성공!!!");

//        try {
//            channelService.delete(channelId);
//            return ResponseEntity.status(HttpStatus.OK).body("[From. Server] 채널 삭제 성공!!!");
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("[From. Server] 채널 삭제 실패 - 존재하지 않는 채널 ID: " + channelId);
//        }
    }

    @RequestMapping(
            path = "/search"
            , method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<List<ChannelDto>> getUserChannels(
            @RequestParam("userId") UUID userId
    ) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
