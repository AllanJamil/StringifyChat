package se.nackademin.stringify.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    MessageRepository messageRepository;

    @Mock
    ChatSessionRepository chatSessionRepository;

    @InjectMocks
    MessageService messageService;

    @DisplayName("Non existent chatSession should throw ChatSessionNotFoundException")
    @Test
    void TestGetMessageThrowsException() {
        given(chatSessionRepository.existsByGuid(any(UUID.class))).willReturn(false);

        assertThatThrownBy(() -> messageService.getMessage(UUID.randomUUID(), 0))
                .isInstanceOf(ChatSessionNotFoundException.class);
    }

    @DisplayName("Existing chat session should return list of messages")
    @Test
    void TestGetMessageShouldReturnListOfMessages() throws ChatSessionNotFoundException {
        List<Message> mockMessages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockMessages.add(new Message());
        }

        given(chatSessionRepository.existsByGuid(any(UUID.class))).willReturn(true);

        given(messageRepository.findAllByChatSession_Guid(any(UUID.class), any())).willReturn(mockMessages);

        List<Message> message = messageService.getMessage(UUID.randomUUID(), 0);

        assertThat(message).isNotEmpty();
    }
}