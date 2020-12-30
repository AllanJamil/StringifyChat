package se.nackademin.stringify.util;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateConverter {

    //TODO add documentation
    private String PATTERN = "yyyy-MM-dd HH:mm";

    public Timestamp stringToDate(String dateValue) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        LocalDateTime dateTime = LocalDateTime.parse(dateValue, formatter);
        return Timestamp.valueOf(dateTime);
    }

    public String dateToString(Timestamp date) {
        int index = date.toString().lastIndexOf(":");
        return date.toString().substring(0, index);
    }
}
