package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {

    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();
    String content = messageCreateRequest.content();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " does not exist"));

    User author = userRepository.findById(authorId)
        .orElseThrow(
            () -> new NoSuchElementException("Author with id " + authorId + " does not exist"));

    List<BinaryContent> attachments = new ArrayList<>();
    for (BinaryContentCreateRequest attachmentRequest : binaryContentCreateRequests) {
      BinaryContent binaryContent = BinaryContent.builder()
          .fileName(attachmentRequest.fileName())
          .size((long) attachmentRequest.bytes().length)
          .contentType(attachmentRequest.contentType())
          .build();

      BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
      binaryContentStorage.put(savedBinaryContent.getId(), attachmentRequest.bytes());
      attachments.add(savedBinaryContent);
    }

    Message message = Message.builder()
        .content(content)
        .channel(channel)
        .author(author)
        .attachments(attachments)
        .build();

    Message savedMessage = messageRepository.save(message);

    return messageMapper.toDto(savedMessage);
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID messageId) {

    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
        pageable.getPageSize(),
        Sort.by("createdAt").descending()
    );

    Page<Message> messagePage = messageRepository.findAllWithAuthorAndChannelByChannelId(channelId,
        pageable);

    return messagePage.map(messageMapper::toDto);
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();

    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.update(newContent);

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    messageRepository.delete(message);
  }

  // MessageService에 추가할 메소드 예시
  @Override
  @Transactional(readOnly = true)
  public Page<MessageDto> findAllByChannelIdWithCursor(UUID channelId, LocalDateTime cursor,
      Pageable pageable) {

    Page<Message> messagePage;

    if (cursor != null) {
      // 커서 이후의 데이터만 조회
      messagePage = messageRepository.findByChannelIdAndCreatedAtBeforeOrderByCreatedAtDesc(
          channelId, cursor, pageable);
    } else {
      // 첫 페이지 조회
      messagePage = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);
    }

    return messagePage.map(messageMapper::toDto);
  }


}
