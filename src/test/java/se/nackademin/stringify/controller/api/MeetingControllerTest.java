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
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.util.Key;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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

    private ChatSession chatSession;
    private ProfileDto profileDto;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        UUID guid = UUID.randomUUID();
        chatSession = ChatSession.builder()
                .id(UUID.randomUUID())
                .guid(guid)
                .key(Key.generate().toString())
                .connectUrl("https://stringify-chat.netlify.app/profile?connect=" + guid)
                .build();

        profileDto = ProfileDto.builder()
                .avatar("avatar1")
                .name("John Doe")
                .build();


    }

    @DisplayName("The given profile should get persisted to DB together with a ChatSession")
    @Test
    void profileShouldExist_AndCreateNewChatSession_OnDataBase() throws Exception {

        String jsonProfile = new ObjectMapper().writeValueAsString(profileDto);
        MvcResult mvcResult = mockMvc.perform(post("/api/meetings/new-meeting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProfile))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        Meeting meeting = new ObjectMapper().readValue(contentAsString, Meeting.class);

        ChatSession chatSession = chatSessionRepository.findByKey(meeting.getChatSession().getKey()).get();

        assertThat(chatSession).isNotNull();
        assertThat(chatSession.getProfilesConnected().size()).isEqualTo(1);
    }

    @DisplayName("Empty json and empty json object should return BAD REQUEST")
    @ParameterizedTest
    @ValueSource(strings = {"", "{}", "{\"name\": \"\", \"avatar\": \"avatar1\"}"})
    void emptyArgumentShouldReturnBadRequest(String argument) throws Exception {

        mockMvc.perform(post("/api/meetings/new-meeting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(argument))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

/*    @Test
    void joinWithKey() {
    }

    @Test
    void joinWithGuid() {
    }*/
}
