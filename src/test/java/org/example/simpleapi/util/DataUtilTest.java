package org.example.simpleapi.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DataUtilTest {

    @Test
    public void offsetDateTimeParseTest() {
        String testDateTimeDaylightSaving = "2024-04-05 09:00:00 -05:00";
        OffsetDateTime offsetDateTime = DateUtil.getOffsetDateTimeFromString(testDateTimeDaylightSaving);
        assertThat(offsetDateTime.toString()).contains("2024-04-05T09:00");
    }

    @Test
    public void localDateFromOffset_regularCaseTest() {
        String testDate = "2024-04-05 09:00:00 -04:00";
        OffsetDateTime offsetDateTime = DateUtil.getOffsetDateTimeFromString(testDate);

        LocalDate localDate = DateUtil.getLocalDateFromOffsetDateTime(offsetDateTime);
        assertThat(localDate).isEqualTo("2024-04-05");
    }

    @Test
    public void testLocalDateFromOffset_beforeShift_timeZoneTest() {
        String testDate = "2024-04-05 23:59:00 -05:00";
        OffsetDateTime offsetDateTime = DateUtil.getOffsetDateTimeFromString(testDate);

        LocalDate localDate = DateUtil.getLocalDateFromOffsetDateTime(offsetDateTime);
        assertThat(localDate).isEqualTo("2024-04-05");
    }

    @Test
    public void testLocalDateFromOffset_afterShift_timeZoneTest() {
        String testDate = "2024-04-05 00:01:00 -04:00";
        OffsetDateTime offsetDateTime = DateUtil.getOffsetDateTimeFromString(testDate);

        LocalDate localDate = DateUtil.getLocalDateFromOffsetDateTime(offsetDateTime);
        assertThat(localDate).isEqualTo("2024-04-05");
    }
}
