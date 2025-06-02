package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  MessageDto find(UUID messageId);

  Page<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);

  MessageDto update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);

  Page<MessageDto> findAllByChannelIdWithCursor(UUID channelId, LocalDateTime cursor,
      Pageable pageable);

}
