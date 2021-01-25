package se.nackademin.stringify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.domain.Profile;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.MessageRepository;
import se.nackademin.stringify.repository.ProfileRepository;
import se.nackademin.stringify.util.Key;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LiveCommunicationServiceTest {

    @InjectMocks
    LiveCommunicationService liveCommunicationService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    ChatSessionRepository chatSessionRepository;

    @Mock
    ProfileRepository profileRepository;

    private ChatSession mockChatSession;

    private Profile mockProfile;

    @BeforeEach
    void setUp() {
        UUID chatId = UUID.randomUUID();
        mockChatSession = ChatSession.builder()
                .guid(chatId)
                .key(Key.generate().toString())
                .connectUrl("https://stringify-chat.netlify.app/connect?chat-id="+chatId)
                .build();

        mockProfile = Profile.builder()
                .guid(UUID.randomUUID())
                .name("John Doe")
                .avatar("avatar1")
                .build();

    }

    @Test
    void storeMessage() {
        given(chatSessionRepository.findByGuid(any(UUID.class))).willReturn()

    }

    @Test
    void storeProfileConnected() {
    }

    @Test
    void removeProfileDisconnected() {
    }

    @Test
    void getChatSession() {
    }
}
