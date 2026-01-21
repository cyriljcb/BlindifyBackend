package com.cyriljcb.blindify.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BlindtestStateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_state_when_blindtest_is_active() throws Exception {
        // 1️⃣ Start blindtest
        String startRequest = """
            {
                "playlistId": "fake-playlist",
                "tracks": 2
            }
            """;

        mockMvc.perform(post("/blindtest/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(startRequest))
                .andExpect(status().isNoContent());

        // 2️⃣ Get state
        mockMvc.perform(get("/blindtest/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackCount").value(2))
                .andExpect(jsonPath("$.currentTrackIndex").value(0))
                .andExpect(jsonPath("$.finished").value(false));
    }

    @Test
    void should_return_409_when_no_blindtest_is_active() throws Exception {
        mockMvc.perform(get("/blindtest/state"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("NO_ACTIVE_BLINDTEST"))
                .andExpect(jsonPath("$.status").value(409));
    }
}
