package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FakeBinaryContentStorage implements BinaryContentStorage {

  @Override
  public UUID put(UUID id, byte[] data) {
    System.out.println("임시 저장: " + id);
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    throw new UnsupportedOperationException("미구현");
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto dto) {
    return ResponseEntity.ok("임시 다운로드 응답");
  }
}

