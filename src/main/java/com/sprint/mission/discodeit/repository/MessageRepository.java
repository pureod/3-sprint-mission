package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  Page<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  void deleteAllByChannelId(UUID channelId);

  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);

}
