package se.nackademin.stringify.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.dto.MessageDto;
import se.nackademin.stringify.service.MessageService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("latest")
    public List<MessageDto> latestMessageHistory(@RequestParam(name = "chatid") UUID chatGuid) {
        return messageService.getLatestMessages(chatGuid).stream()
                .map(Message::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("previous/{page}")
    public List<MessageDto> providePreviousMessages(@RequestParam(name = "chatid") UUID chatGuid, @PathVariable int page) {
        return messageService.getPreviousMessage(chatGuid, page).stream()
                .map(Message::convertToDto)
                .collect(Collectors.toList());
    }
}
