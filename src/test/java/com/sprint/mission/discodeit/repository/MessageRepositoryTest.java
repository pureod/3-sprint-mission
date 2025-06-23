package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sprint.mission.discodeit.config.TestJpaConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
public class MessageRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserStatusRepository userStatusRepository;

    @Nested
    @DisplayName("채널 ID 기반 메시지 삭제")
    class DeleteAllByChannelIdTests {

        @Test
        @DisplayName("채널 ID를 통해 메세지들을 삭제할 수 있다")
        void deleteAllByChannelId_should_delete_messages() {
            //Given
            User author = new User("ljy", "ljy@naver.com", "pw123!!", null);
            Channel channel1 = new Channel(ChannelType.PUBLIC, "채널1", "설명1");
            Channel channel2 = new Channel(ChannelType.PUBLIC, "채널2", "설명2");
            Message message1 = new Message("클린 코드", channel1, author, null);
            Message message2 = new Message("대규모 서비스를 지탱하는 기술", channel2, author, null);

            userRepository.save(author);
            channelRepository.saveAll(List.of(channel1, channel2));
            messageRepository.saveAll(List.of(message1, message2));

            // When
            messageRepository.deleteAllByChannelId(channel1.getId());

            // Then
            List<Message> remainingMessages = messageRepository.findAll();
            assertThat(remainingMessages.size()).isEqualTo(1);
            assertThat(remainingMessages.get(0).getContent()).isEqualTo("대규모 서비스를 지탱하는 기술");
        }

        @Test
        @DisplayName("존재하지 않는 채널 ID로 삭제 시 예외 없이 기존 메시지는 유지된다")
        void deleteAllByNonexistentChannelId_should_not_affect_existing_messages() {
            // Given
            User author = new User("ljy", "ljy@naver.com", "pw123!!", null);
            Channel channel = new Channel(ChannelType.PUBLIC, "채널", "설명");
            Message message = new Message("메시지 내용", channel, author, null);

            userRepository.save(author);
            channelRepository.save(channel);
            messageRepository.save(message);

            // When
            messageRepository.deleteAllByChannelId(UUID.randomUUID());

            // Then
            List<Message> remainingMessages = messageRepository.findAll();
            assertThat(remainingMessages.size()).isEqualTo(1);
            assertThat(remainingMessages.get(0).getContent()).isEqualTo("메시지 내용");
        }
    }

    @Nested
    @DisplayName("커서 기반 페이지네이션 테스트")
    class CursorPageableTests {

        @Test
        @DisplayName("커서 시간 이전 메시지를 지정 개수만큼 조회하고 hasNext가 true인 경우")
        void findAllByChannelIdWithAuthor_should_return_slice_before_createdAt_with_hasNext_true() {
            // Given
            User author = new User("tester", "test@example.com", "pw1234!!", null);

            Channel channel = new Channel(ChannelType.PUBLIC, "테스트 채널", "채널 설명");

            Message message1 = new Message("메시지 1", channel, author, null);
            Message message2 = new Message("메시지 2", channel, author, null);
            Message message3 = new Message("메시지 3", channel, author, null);

            userRepository.save(author);
            userStatusRepository.save(new UserStatus(author, Instant.now()));
            channelRepository.save(channel);
            messageRepository.saveAll(List.of(message1, message2, message3));

            Instant cursorTime = Instant.now().plusSeconds(1);
            Pageable pageable = PageRequest.of(0, 2);

            // When
            var resultSlice = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
                cursorTime, pageable);

            // Then
            assertThat(resultSlice.getContent().size()).isEqualTo(2);
            assertThat(resultSlice.hasNext()).isTrue();
        }

        @Test
        @DisplayName("커서 시간 이전의 전체 메시지를 조회하고 hasNext가 false인 경우")
        void findAllByChannelIdWithAuthor_should_return_slice_before_createdAt_with_hasNext_false() {
            // Given
            User author = new User("tester", "test@example.com", "pw1234!!", null);

            Channel channel = new Channel(ChannelType.PUBLIC, "테스트 채널", "채널 설명");

            Message message1 = new Message("메시지 1", channel, author, null);
            Message message2 = new Message("메시지 2", channel, author, null);
            Message message3 = new Message("메시지 3", channel, author, null);

            userRepository.save(author);
            userStatusRepository.save(new UserStatus(author, Instant.now()));
            channelRepository.save(channel);
            messageRepository.saveAll(List.of(message1, message2, message3));

            Instant cursorTime = Instant.now().plusSeconds(1);
            Pageable pageable = PageRequest.of(0, 3);

            // When
            var resultSlice = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
                cursorTime, pageable);

            // Then
            assertThat(resultSlice.getContent().size()).isEqualTo(3);
            assertThat(resultSlice.hasNext()).isFalse();
        }

        @Test
        @DisplayName("메시지를 조회한 후 커서를 옮겼을 때 다음 페이지가 정확히 조회되는 경우")
        void findAllByChannelIdWithAuthor_should_return_slice_before_createdAt_with_correct_next_page() {
            // Given
            User author = new User("tester", "test@example.com", "pw1234!!", null);
            userRepository.save(author);
            userStatusRepository.save(new UserStatus(author, Instant.now()));

            Channel channel = new Channel(ChannelType.PUBLIC, "테스트 채널", "채널 설명");
            channelRepository.save(channel);

            Message message1 = new Message("메시지 1", channel, author, null);
            messageRepository.saveAndFlush(message1);
            Message message2 = new Message("메시지 2", channel, author, null);
            messageRepository.saveAndFlush(message2);
            Message message3 = new Message("메시지 3", channel, author, null);
            messageRepository.saveAndFlush(message3);

            Instant cursorTime = truncateToMicros(message2.getCreatedAt());
            Pageable pageable = PageRequest.of(0, 3);

            // When
            var resultSlice = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
                cursorTime, pageable);

            // Then
            assertThat(resultSlice.getContent().size()).isEqualTo(1);
            assertThat(resultSlice.getContent().get(0).getContent()).isEqualTo("메시지 1");
            assertThat(resultSlice.hasNext()).isFalse();
        }

        private Instant truncateToMicros(Instant instant) {
            long micros = instant.getEpochSecond() * 1_000_000 + instant.getNano() / 1_000;
            return Instant.ofEpochSecond(micros / 1_000_000, (micros % 1_000_000) * 1_000);
        }

    }

}
