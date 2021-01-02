package se.nackademin.stringify.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import se.nackademin.stringify.AbstractIntegrationTest;
import se.nackademin.stringify.controller.response.Meeting;
import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.domain.Profile;
import se.nackademin.stringify.dto.ChatSessionDto;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.ProfileRepository;
import se.nackademin.stringify.util.Key;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MeetingControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ChatSessionRepository chatSessionRepository;

    @Autowired
    ProfileRepository profileRepository;

    private ChatSession chatSession;
    private ProfileDto profileDto;
    private List<Profile> profilesConnected;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        UUID guid = UUID.randomUUID();
        this.chatSession = ChatSession.builder()
                .id(UUID.randomUUID())
                .guid(guid)
                .key(Key.generate().toString())
                .connectUrl("https://stringify-chat.netlify.app/profile?connect=" + guid)
                .build();

        this.chatSession = chatSessionRepository.save(this.chatSession);

        this.profileDto = ProfileDto.builder()
                .avatar("avatar1")
                .name("John Doe")
                .build();

        this.profilesConnected = List.of(
                Profile.builder()
                        .name("John Doe")
                        .avatar("avatar16")
                        .id(UUID.randomUUID())
                        .guid(UUID.randomUUID()).build(),
                Profile.builder()
                        .name("Jake Fish")
                        .avatar("avatar10")
                        .id(UUID.randomUUID())
                        .guid(UUID.randomUUID()).build(),
                Profile.builder()
                        .name("Laura")
                        .avatar("avatar1")
                        .id(UUID.randomUUID())
                        .guid(UUID.randomUUID()).build(),
                Profile.builder()
                        .name("Sarah")
                        .avatar("avatar19")
                        .id(UUID.randomUUID())
                        .guid(UUID.randomUUID()).build(),
                Profile.builder()
                        .name("Brad")
                        .avatar("avatar3")
                        .id(UUID.randomUUID())
                        .guid(UUID.randomUUID()).build()
        );


    }

    @DisplayName("The given profile should get persisted to DB together with a ChatSession")
    @Test
    void profileShouldExist_AndCreateNewChatSession_OnDataBase() throws Exception {

        String jsonProfile = new ObjectMapper().writeValueAsString(this.profileDto);
        MvcResult mvcResult = mockMvc.perform(post("/api/meetings/new-meeting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProfile))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        Meeting meeting = new ObjectMapper().readValue(contentAsString, Meeting.class);

        ChatSession chatSession = this.chatSessionRepository.findByKey(meeting.getChatSession().getKey()).get();

        assertThat(chatSession).isNotNull();
        assertThat(chatSession.getProfilesConnected().size()).isEqualTo(1);
    }

    @DisplayName("Invalid argument should return BAD REQUEST")
    @ParameterizedTest
    @ValueSource(strings = {"", "{}", "{\"name\": \"\", \"avatar\": \"avatar1\"}"})
    void invalidArgumentShouldReturnBadRequest(String argument) throws Exception {

        mockMvc.perform(post("/api/meetings/new-meeting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(argument))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("Valid key with existing ChatSession should return dto")
    @Test
    void validKeyWithExistingChatSessionShouldReturnDto() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/meetings/join-meeting/key/" + this.chatSession.getKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ChatSessionDto chatSessionDto = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), ChatSessionDto.class);

        assertThat(chatSessionDto).isNotNull();
        assertThat(chatSessionDto.getKey()).isEqualTo(this.chatSession.getKey());
    }

    @DisplayName("valid Key with non existing ChatSession should return NOT FOUND")
    @Test
    void validKeyNonExistingChatSessionShouldReturnNOT_FOUND() throws Exception {
        mockMvc.perform(get("/api/meetings/join-meeting/key/" + Key.generate())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("valid Key and ChatSession with max number of connections Should return NO CONTENT")
    @Test
    void validKeyAndChatSessionWithMaxConnectionShouldReturnNO_CONTENT() throws Exception {


        profilesConnected.forEach(profile -> {
            profile.setChatSession(chatSession);
            profileRepository.save(profile);
        });

        MvcResult mvcResult = mockMvc.perform(get("/api/meetings/join-meeting/key/" + this.chatSession.getKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        assertThat(mvcResult.getResponse().getErrorMessage())
                .isEqualTo("Meeting has reached the maximum number of connections.");
    }

    @DisplayName("Invalid key Should Return BAD REQUEST")
    @Test
    void invalidKeyShouldReturnBAD_REQUEST() throws Exception {
        String invalidKey = "da984dq6qaaaaaa";

        MvcResult mvcResult = mockMvc.perform(get("/api/meetings/join-meeting/key/" + invalidKey)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertThat(mvcResult.getResponse().getErrorMessage())
                .isEqualTo("Invalid key: Incorrect argument cannot be handled");
    }

    @DisplayName("Chat id should with existing ChatSession should return dto")
    @Test
    void chatIdWithExistingChatSessionShouldReturnDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/meetings/join-meeting/chat-id/" + chatSession.getGuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ChatSessionDto chatSessionDto = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), ChatSessionDto.class);

        assertThat(chatSessionDto).isNotNull();
        assertThat(chatSessionDto.getGuid()).isEqualTo(this.chatSession.getGuid());
    }

    @DisplayName("Chat id with non existing chat session should return NOT FOUND")
    @Test
    void chatIdWithNonExistingChatSessionShouldReturnNOT_FOUND() throws Exception {
        mockMvc.perform(get("/api/meetings/join-meeting/chat-id/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void chatIdAndChatSessionWithMaxConnectionShouldReturnNO_CONTENT() throws Exception {

        this.profilesConnected.forEach(profile -> {
            profile.setChatSession(this.chatSession);
            profileRepository.save(profile);
        });

        mockMvc.perform(get("/api/meetings/join-meeting/chat-id/" + this.chatSession.getGuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
