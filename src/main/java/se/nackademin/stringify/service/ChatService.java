package se.nackademin.stringify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.MessageRepository;

import javax.validation.Valid;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final ChatSessionRepository chatSessionRepository;

    public Message storeMessage(UUID chatSessionId, @Valid Message message) throws ChatSessionNotFoundException {
        ChatSession chatSession = chatSessionRepository.findByGuid(chatSessionId)
                .orElseThrow(() -> new ChatSessionNotFoundException(
                        "Chat session with id " + chatSessionId + " does not exists."
                ));

        message.setChatSession(chatSession);
        return messageRepository.save(message);
    }

}
