package se.nackademin.stringify.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Timestamp;
import java.time.format.DateTimeParseException;
import java.util.Date;

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

    @DisplayName("A value with incorrect format should throw IllegalArgumentException")
    @ParameterizedTest
    @ValueSource(strings = {"2020-12-30", "2020-18-60 15:62", "2020-12-30     14:01", "2020-12-30T14:01"})
    void IncorrectFormatWillThrowIllegalArgumentException(String incorrectFormat) {
        assertThatThrownBy(() -> DateConverter.stringToDate(incorrectFormat)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Null value should throw IllegalArgumentException")
    @Test
    void nullValueShouldThrowIllegalArgumentException() {

        assertThatThrownBy(() -> DateConverter.stringToDate(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("the given Timestamp object should be converted to an instance of a String")
    @Test
    void dateShouldReturnAStringInstance() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        assertThat(DateConverter.dateToString(timestamp)).isInstanceOf(String.class);
    }

    @DisplayName("null Date should throw IllegalArgumentException")
    @Test
    void nullDateShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> DateConverter.dateToString(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Timestamp converted to String should have format yyyy-MM-dd HH:mm")
    @Test
    void stringDateShouldHaveSameValueAsTimestamp() {
        Timestamp timestamp = Timestamp.valueOf("2020-09-15 15:16:00");

        String actual = DateConverter.dateToString(timestamp);

        assertThat(actual).isEqualTo("2020-09-15 15:16");
        assertThat(actual.matches("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]$")).isTrue();


    }
}
