package org.example.simpleapi.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private final static int MAX_PARSABLE_LENGTH = 19;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);

    public static OffsetDateTime getOffsetDateTimeFromString(String dateString) {
        if (dateString.length() > MAX_PARSABLE_LENGTH) {
            dateString = dateString.substring(0, MAX_PARSABLE_LENGTH);
        }
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        return OffsetDateTime.of(dateTime, ZoneOffset.UTC);
    }

}
