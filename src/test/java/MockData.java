import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.domain.Profile;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.util.Key;

import java.util.UUID;

/**
 * A class for providing mock data for tests.
 */
public class MockData {

    public static ChatSession getMockChatSessionEntity() {
        UUID chatId = UUID.randomUUID();
        return  ChatSession.builder()
                .guid(chatId)
                .key(Key.generate().toString())
                .connectUrl("https://stringify-chat.netlify.app/connect?chat-id="+chatId)
                .build();
    }

    /**
     * Method provides a mocked profile without relation to Chatsession
     * @return {@code Profile.class} Mocked Profile
     */
    public static Profile getMockProfileEntity() {
        return Profile.builder()
                .id(UUID.randomUUID())
                .guid(UUID.randomUUID())
                .name("John Doe")
                .avatar("avatar1")
                .build();
    }

    public static ProfileDto getMockProfileDto() {

    }
}
