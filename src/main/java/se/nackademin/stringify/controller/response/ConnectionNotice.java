package se.nackademin.stringify.controller.response;

import lombok.Getter;
import lombok.Setter;
import se.nackademin.stringify.dto.MessageDto;
import se.nackademin.stringify.dto.ProfileDto;

@Getter
@Setter
public class ConnectionNotice {

    private ProfileDto profile;
    private MessageDto connectionMessage;

    public ConnectionNotice(ProfileDto profile, MessageDto connectionMessage) {
        this.profile = profile;
        this.connectionMessage = connectionMessage;
    }
}
