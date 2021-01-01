package se.nackademin.stringify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.repository.MessageRepository;

import java.util.List;
import java.util.UUID;


/**
 * A service class used for handling messages.
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * Fetches a list of maximum 5 message objects from the database with use of Pagination.
     * The list is sorted by ascending dates.
     *
     * @param chatGuid the {@code UUID} chat guid in relation with the messages
     * @param page The of page number of a list of messages
     * @return A list of messages ({@code List<Message>}) with a size of 5
     */
    @Transactional(readOnly = true)
    public List<Message> getMessage(UUID chatGuid, int page) {
        final int AMOUNT_OF_ELEMENTS = 5;

        Pageable sortedByDate =
                PageRequest.of(page, AMOUNT_OF_ELEMENTS, Sort.by("date").ascending());

        return messageRepository.findAllByChatSession_Guid(chatGuid, sortedByDate);
    }

}
