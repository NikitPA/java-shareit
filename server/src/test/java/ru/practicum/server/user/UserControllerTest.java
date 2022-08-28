package ru.practicum.server.user;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.user.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUsers() throws Exception {
        User user = mock(User.class);
        doReturn(List.of(user)).when(userService).getAllUsers(0, 10);

        this.mockMvc
                .perform(get("/users"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(1L);

        this.mockMvc
                .perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserByIdFailedUserNotFound() throws Exception {
        doReturn(Optional.empty()).when(userService).getUserById(1L);

        this.mockMvc
                .perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testCreateUser() throws Exception {
        String json = "{\"name\":\"name\", \"email\":\"email@email.com\"}";

        try (MockedStatic<UserMapper> utilities = mockStatic(UserMapper.class)) {
            utilities.when(() -> UserMapper.toUserDto(mock(User.class)))
                    .thenReturn(mock(UserDto.class));

            this.mockMvc
                    .perform(post("/users")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }
    }

    @Test
    void testUpdateUser() throws Exception {
        String json = "{\"name\":\"name\", \"email\":\"email@email.ru\"}";

        try (MockedStatic<UserMapper> utilities = mockStatic(UserMapper.class)) {
            utilities.when(() -> UserMapper.toUserDto(mock(User.class)))
                    .thenReturn(mock(UserDto.class));

            this.mockMvc
                    .perform(patch("/users/1")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testUpdateUserFailedUserNotFound() throws Exception {
        String json = "{\"name\":\"name\", \"email\":\"email@email.ru\"}";


        //doReturn()
        doThrow(UserNotFoundException.class).when(userService)
                .updateUser(1L, Map.of("name", "name", "email", "email@email.ru"));

        this.mockMvc
                .perform(patch("/users/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        this.mockMvc
                .perform(delete("/users/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserFailedUserNotFound() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).deleteUser(1L);

        this.mockMvc
                .perform(delete("/users/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
