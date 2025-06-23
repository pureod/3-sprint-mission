package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.entity.BinaryContent;

public class BinaryContentFixture {

    public static BinaryContent createValid() {
        return new BinaryContent(
            "sample-image.png",
            2048L,
            "image/png"
        );
    }
}