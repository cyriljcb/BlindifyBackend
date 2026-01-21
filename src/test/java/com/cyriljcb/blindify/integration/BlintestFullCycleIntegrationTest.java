package com.cyriljcb.blindify.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BlindtestFullCycleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_run_complete_blindtest_cycle() throws Exception {

        // 1️ START
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

        // 2️ STATE - début
        mockMvc.perform(get("/blindtest/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTrackIndex").value(0))
                .andExpect(jsonPath("$.trackCount").value(2))
                .andExpect(jsonPath("$.finished").value(false));

        // // 3️ NEXT TRACK
        mockMvc.perform(post("/blindtest/playback/next"))
                .andExpect(status().isNoContent());

        // // 4️ STATE - après next
        mockMvc.perform(get("/blindtest/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTrackIndex").value(1))
                .andExpect(jsonPath("$.finished").value(false));

        // // 5️ NEXT → fin
        mockMvc.perform(post("/blindtest/playback/next"))
                .andExpect(status().isNoContent());

        // // 6️ STATE - terminé
        mockMvc.perform(get("/blindtest/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finished").value(true));
    }
}