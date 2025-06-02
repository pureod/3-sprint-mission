package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return PageResponse.<T>builder()
        .content(slice.getContent())
        .nextCursor(calculateNextCursor(slice))
        .size(slice.getSize())
        .hasNext(slice.hasNext())
        .totalElements(null)
        .build();
  }

  public <T> PageResponse<T> fromPage(Page<T> page) {
    return PageResponse.<T>builder()
        .content(page.getContent())
        .nextCursor(calculateNextCursor(page))
        .size(page.getSize())
        .hasNext(page.hasNext())
        .totalElements(page.getTotalElements())
        .build();
  }

  private <T> Object calculateNextCursor(Slice<T> slice) {
    if (!slice.hasNext() || slice.getContent().isEmpty()) {
      return null;
    }

    // 마지막 요소를 기준으로 커서 생성
    // 예: Message의 경우 createdAt 값을 커서로 사용
    T lastElement = slice.getContent().get(slice.getContent().size() - 1);
    if (lastElement instanceof MessageDto) {
      return ((MessageDto) lastElement).createdAt();
    }

    // 여기서 실제 커서 값을 추출하는 로직이 필요합니다
    // 예시: MessageDto의 경우
    // if (lastElement instanceof MessageDto) {
    //   return ((MessageDto) lastElement).getCreatedAt();
    // }

    return null; // 임시로 null 반환
  }

}
