package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentEventListener {

    private final BinaryContentStorage binaryContentStorage;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BinaryContentCreatedEvent event) {

        UUID binaryContentId = event.binaryContentId();
        byte[] bytes = event.bytes();

        log.debug("[BinaryContentCreatedEventListener] AFTER_COMMIT - store id={} size={}",
            binaryContentId, bytes == null ? 0 : bytes.length);

        binaryContentStorage.put(binaryContentId, bytes);

        log.debug("[BinaryContentCreatedEventListener] AFTER_COMMIT - store complete");
    }
}
