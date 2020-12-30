package se.nackademin.stringify.controller.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.dto.MessageDto;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.service.ChatService;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("send/meeting/{chatSessionGuid}")
    @SendTo("queue/meeting/{chatSessionGuid}")
    public MessageDto transmit(@DestinationVariable UUID chatSessionGuid, @Payload @Valid MessageDto messageDto) {
        Message message = messageDto.convertToEntity();
        try {
            return chatService.storeMessage(chatSessionGuid, message).convertToDto();
        } catch (ChatSessionNotFoundException e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
