package se.nackademin.stringify.util;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * A utility class that converts/parses date to a representational formatted string (<em>yyyy-MM-dd HH:mm</em>)
 * or vice versa.
 * <br />
 * <br />
 * Data type of date is a {@code java.sql.Timestamp} for the purpose of simplifying Entities persisting to database.
 */
@UtilityClass
public class DateUtil {

    /**
     * String pattern to be converted to Timestamp
     */
    private final String PATTERN = "yyyy-MM-dd HH:mm";
    private final String DATE_REGEX = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]$";

    /**
     * Converts the string representation of a date to a {@code java.sql.Timestamp}
     *
     * @param dateValue string representation of date to be converted
     * @return Timestamp of the string value
     * @throws IllegalArgumentException
     */
    public Timestamp stringToDate(String dateValue) throws IllegalArgumentException {
        if (dateValue == null || !dateValue.matches(DATE_REGEX))
            throw new IllegalArgumentException("Invalid format. Date pattern is: " + PATTERN);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);


        LocalDateTime dateTime = LocalDateTime.parse(dateValue, formatter);
        return Timestamp.valueOf(dateTime);
    }

    /**
     * Converts a {@code java.sql.Timestamp} object to a string date with the pattern of <li>yyyy-MM-dd HH:mm</li>
     *
     * @param date Timestamp to be converted to a String representation of a date
     * @return Representational string date of the given Timestamp
     * @throws IllegalArgumentException
     */
    public String dateToString(Timestamp date) throws IllegalArgumentException {
        if (date == null)
            throw new IllegalArgumentException("Cannot convert null to String");

        String stringDate = date.toLocalDateTime().toString();

        return stringDate.replace("T", " ")
                .substring(0, stringDate.lastIndexOf(":"));
    }

    public String now() {
       return dateToString(new Timestamp(new Date().getTime()));
    }

}
