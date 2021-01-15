package se.nackademin.stringify.controller.response;

import lombok.Getter;
import lombok.Setter;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.util.DateConverter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class ConnectionNotice {

    private ProfileDto profile;
    private String connectionMessage;
    private String date;

    public ConnectionNotice(ProfileDto profile, String connectionMessage) {
        this.profile = profile;
        this.connectionMessage = connectionMessage;
        date = DateConverter.dateToString(new Timestamp(new Date().getTime()));
    }
}
