package se.nackademin.stringify.util;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * A utility class that converts/parses date to a representational formatted string (<em>yyyy-MM-dd HH:mm</em>)
 * or vice versa.
 * <br />
 * <br />
 * Data type of date is a {@code java.sql.Timestamp} for the purpose of simplifying Entities persisting to database.
 */
@UtilityClass
public class DateConverter {

    /**
     * String pattern to be converted to Timestamp
     */
    private String PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * Converts the string representation of a date to a {@code java.sql.Timestamp}
     *
     * @param dateValue string representation of date to be converted
     * @return Timestamp of the string value
     */
    public Timestamp stringToDate(String dateValue) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        LocalDateTime dateTime = LocalDateTime.parse(dateValue, formatter);
        return Timestamp.valueOf(dateTime);
    }

    /**
     * Converts a {@code java.sql.Timestamp} object to a string date with the pattern of <li>yyyy-MM-dd HH:mm</li>
     *
     * @param date Timestamp to be converted to a String representation of a date
     * @return Representational string date of the given Timestamp
     */
    public String dateToString(Timestamp date) {
        return date.toLocalDateTime().toString().replace("T", " ");
    }
}
