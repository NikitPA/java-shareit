package ru.practicum.gateway.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllItemRequestFailedNegativeParam() throws Exception {
        this.mockMvc
                .perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 3)
                        .param("from", "-5")
                        .param("size", "4"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testCreatedItemRequestFailed() throws Exception {
        String jsonContent = "{\"description\":\"\"}";

        this.mockMvc
                .perform(post("/requests")
                        .header("X-Sharer-User-Id", 3)
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }
}
