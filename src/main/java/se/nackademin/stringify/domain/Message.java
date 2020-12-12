package se.nackademin.stringify.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter

public class Message {

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String Sender;

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 1000)
    private String content;

    private String picturePath;


}
