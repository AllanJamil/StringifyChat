package se.nackademin.stringify.controller.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.dto.MessageDto;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.service.MessageService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/messages")
public class MessageController {

    private final MessageService messageService;


    @ApiOperation(
            value = "Find a number of messages by chat-id and page number",
            produces = "application/json",
            notes = "Returns a collection of 5 messages in the given page number from a chat session",
            response = MessageDto.class
    )
    @GetMapping("history")
    public List<MessageDto> getHistoryMessages(@RequestParam(name = "chat-id") UUID chatGuid, @RequestParam int page) {
        try {
            return messageService.getMessage(chatGuid, page).stream()
                    .map(Message::convertToDto)
                    .collect(Collectors.toList());
        } catch (ChatSessionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
