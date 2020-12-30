package se.nackademin.stringify.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateConverterTest {

    @DisplayName("A value with yyyy-MM-dd HH:mm should return Timestamp with same value")
    @Test
    void correctFormatWillReturnTimeStampWithSameValue() {
        String value = "2020-12-30 14:01";
        Timestamp timestamp = DateConverter.stringToDate(value);

        assertThat(timestamp.toLocalDateTime().toString()).isEqualTo(value.replace(" ", "T"));
    }

    @DisplayName("A value with incorrect format should cause DateTimeParseException")
    @Test
    void IncorrectFormatWillThrowDateTimeParseException() {
        String incorrectFormat = "2020-12-30";
        assertThatThrownBy(() -> DateConverter.stringToDate(incorrectFormat)).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void dateToString() {
    }
}
