package org.example.simpleapi.util;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DataUtilTest {

    @Test
    public void testOffsetDateTimeParse() {
        String testDate = "2024-04-05 09:00:00 -05:00";
        OffsetDateTime offsetDateTime = DateUtil.getOffsetDateTimeFromString(testDate);
        assertThat(offsetDateTime.toString()).isEqualTo("2024-04-05T09:00Z");
    }
}
