package org.example.simpleapi.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private final static int MAX_PARSABLE_LENGTH = 19;

    //Most of the market/trade uses eastern zones
    //-4:00 from UTC during summer time
    //-5:00 from UTC during daylight saving time
    private final static ZoneId easternZone = ZoneId.of("America/New_York");

    //Central zone for zone neutral cases
    private final static ZoneId centralZone = ZoneId.of("UTC");

    private final static DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(easternZone);

    public static OffsetDateTime getOffsetDateTimeFromString(String dateString) {
        if (dateString.length() > MAX_PARSABLE_LENGTH) {
            dateString = dateString.substring(0, MAX_PARSABLE_LENGTH);
        }
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        ZoneOffset zoneOffSet = easternZone.getRules().getOffset(dateTime);
        return OffsetDateTime.of(dateTime, zoneOffSet);
    }

    public static LocalDate getLocalDateFromOffsetDateTime(OffsetDateTime offsetDateTime) {
        ZonedDateTime zoned = offsetDateTime.atZoneSameInstant(easternZone);
        return zoned.toLocalDate();
    }
}
