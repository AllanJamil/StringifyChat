package se.nackademin.stringify.util;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
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
     * Converts a {@code java.sql.Timestamp} object to a string date with the pattern of <li>yyyy-MM-dd HH:mm</li>
     *
     * @param date Timestamp to be converted to a String representation of a date
     * @return Representational string date of the given Timestamp
     * @throws IllegalArgumentException
     */
    public String dateToString(Timestamp date) throws IllegalArgumentException {
        if (date == null)
            throw new IllegalArgumentException("Cannot convert null to String");

        String stringDate = date.toString();

        return stringDate.substring(0, stringDate.lastIndexOf(":"));
    }

    public static String stringValueOfNow() {
       return dateToString(new Timestamp(new Date().getTime()));
    }

    public static Timestamp now() {
        return new Timestamp(new Date().getTime());
    }
}
