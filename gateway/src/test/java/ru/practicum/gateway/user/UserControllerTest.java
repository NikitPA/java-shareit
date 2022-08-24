package ru.practicum.gateway.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserClient userClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateUserFailedNotEmptyName() throws Exception {
        String json = "{\"email\":\"email@email.com\"}";

        this.mockMvc
                .perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testCreateUserFailedNotCorrectEmail() throws Exception {
        String json = "{\"name\":\"name\", \"email\":\"emailemail\"}";

        this.mockMvc
                .perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }
}
