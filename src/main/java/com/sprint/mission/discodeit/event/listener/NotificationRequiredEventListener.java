package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

    private final NotificationService notificationService;
    private final ReadStatusRepository readStatusRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(MessageCreatedEvent event) {

        log.debug("[NotificationRequiredEventListener] 메세지 생성 알림 이벤트 리스너 시작");

        List<UUID> receiverIds = readStatusRepository
            .findUserIdsByChannelIdAndNotificationEnabledTrue(event.channel().getId());

        log.debug("알림을 받는 유저: {}", receiverIds);

        receiverIds.stream()
            .filter(receiverId -> !receiverId.equals(event.author().getId()))
            .forEach(receiverId -> {
                String title = String.format("%s (%s)",
                    event.author().getUsername(), buildTitleName(event.channel())
                );
                String content = event.content();

                notificationService.createNotification(receiverId, title, content);
            });
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(RoleUpdatedEvent event) {

        log.debug("[NotificationRequiredEventListener] 권한 변경 알림 이벤트 리스너 시작");

        String title = "권한이 변경되었습니다.";
        String content = String.format("%s -> %s", event.oldRole(), event.newRole());

        notificationService.createNotification(event.userId(), title, content);
    }

    private static String buildTitleName(Channel channel) {
        return (channel.getName() != null) ? channel.getName() : "Private";
    }
}
