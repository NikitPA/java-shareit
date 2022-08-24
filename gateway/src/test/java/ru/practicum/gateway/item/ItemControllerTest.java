package ru.practicum.gateway.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetItemsFailedNegativeParam() throws Exception {
        this.mockMvc
                .perform(get("/items")
                        .header("X-Sharer-User-Id", 3)
                        .param("from", "-3")
                        .param("size", "3"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testCreateItemFailedNoCorrectBody() throws Exception {
        String json =
                "{\"name\":\"\",\"description\":\"description\",\"available\":true}";

        this.mockMvc
                .perform(post("/items")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testCreateCommentFailedNotCorrectBody() throws Exception {
        String json = "{\"text\":\"\"}";

        this.mockMvc
                .perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 3)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
