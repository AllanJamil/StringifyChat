package se.nackademin.stringify.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import se.nackademin.stringify.dto.ProfileDto;

@Getter
@Setter
@AllArgsConstructor
public class ConnectionNotice {

    private ProfileDto profile;
    private String welcomeMessage;
}
