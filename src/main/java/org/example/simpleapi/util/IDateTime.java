package org.example.simpleapi.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Date time interface to make it easier to mock and test
 */
public interface IDateTime {
    default OffsetDateTime getCurrentOffsetDateTime() {
        return OffsetDateTime.now();
    }

    default LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }
}
