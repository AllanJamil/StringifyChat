package se.nackademin.stringify.controller.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;
import se.nackademin.stringify.controller.response.ConnectionNotification;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.dto.MessageDto;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.service.ChatService;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/send/meeting/{chatSessionGuid}")
    @SendTo("queue/meeting/{chatSessionGuid}")
    public MessageDto transmit(@DestinationVariable UUID chatSessionGuid, @Payload @Valid MessageDto messageDto) {
        Message message = messageDto.convertToEntity();
        try {
            return chatService.storeMessage(chatSessionGuid, message).convertToDto();
        } catch (ChatSessionNotFoundException e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @MessageMapping("/welcome/{chatSessionGuid}")
    @SendTo("queue/welcome/{chatSessionGuid}")
    public ConnectionNotification notifyOnConnect(@DestinationVariable UUID chatSessionGuid, @Payload @Valid ProfileDto profile) {
        try {
            return chatService.storeProfileConnected(
                    chatSessionGuid,
                    profile.convertToEntity());
        } catch (ChatSessionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
