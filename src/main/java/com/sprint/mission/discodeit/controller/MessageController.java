package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.swing.ImageIcon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Tag(name = "Message", description = "Message API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "Message 생성")
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "Message가 성공적으로 생성됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = Message.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found")
          )
      )
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> attachmentRequests = new ArrayList<>();

    if (attachments != null) {
      for (MultipartFile file : attachments) {
        resolveAttachmentRequest(file).ifPresent(attachmentRequests::add);
      }
    }

    Message created = messageService.create(messageCreateRequest, attachmentRequests);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @Operation(summary = "Message 내용 수정")
  @Parameter(name = "messageId", description = "수정할 Message ID", required = true)
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Message가 성공적으로 수정됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = Message.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Message를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject(value = "Message with id {messageId} not found")
          )
      )
  })
  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> update(
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest request
  ) {

    Message updatedmessage = messageService.update(messageId, request);
    return ResponseEntity.status(HttpStatus.OK).body(updatedmessage);

  }

  @Operation(summary = "Message 삭제")
  @Parameter(name = "messageId", description = "삭제할 Message ID", required = true)
  @ApiResponses({
      @ApiResponse(
          responseCode = "204",
          description = "Message가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Message를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject(value = "Message with id {messageId} not found")
          )
      )
  })
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(
      @PathVariable("messageId") UUID messageId
  ) {

    messageService.delete(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

  }

  @Operation(
      summary = "Channel의 Message 목록 조회"
  )
  @Parameter(name = "channelId", description = "조회할 Channel ID", required = true)
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Message 목록 조회 성공",
          content = @Content(
              mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = Message.class))
          )
      )
  })
  @GetMapping
  public ResponseEntity<List<Message>> findAllByChannelId
      (
          @RequestParam("channelId") UUID channelId
      ) {

    List<Message> messages = messageService.findAllByChannelId(channelId);

    return ResponseEntity.status(HttpStatus.OK).body(messages);
  }


  private Optional<BinaryContentCreateRequest> resolveAttachmentRequest(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(new BinaryContentCreateRequest(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      ));
    } catch (IOException e) {
      throw new RuntimeException("첨부파일 처리 중 오류 발생: " + file.getOriginalFilename(), e);
    }
  }

}
