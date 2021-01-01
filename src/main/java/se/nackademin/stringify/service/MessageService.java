package se.nackademin.stringify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private ChatSessionRepository chatSessionRepository;


    @Transactional(readOnly = true)
    public List<Message> getLatestMessages(UUID chatGuid) {
        Pageable sortedByDate =
                PageRequest.of(0, 5, Sort.by("date").ascending());

        return messageRepository.findAllByChatSession_Guid(chatGuid, sortedByDate);
    }

    @Transactional(readOnly = true)
    public List<Message> getPreviousMessage(UUID chatGuid, int page) {
        Pageable sortedByDate =
                PageRequest.of(page, 5, Sort.by("date").ascending());

        return messageRepository.findAllByChatSession_Guid(chatGuid, sortedByDate);
    }

}
