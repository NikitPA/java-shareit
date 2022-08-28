package ru.practicum.server.request;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.server.exception.ItemRequestNotFoundException;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllItemRequestByUser() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        doReturn(List.of()).when(itemRequestService).getAllItemRequestByUser(user);

        try (MockedStatic<ItemRequestMapper> utilities = Mockito.mockStatic(ItemRequestMapper.class)) {
            utilities.when(() -> ItemRequestMapper.toItemRequestDto(Mockito.mock(ItemRequest.class)))
                    .thenReturn(Mockito.mock(ItemRequestDto.class));

            this.mockMvc
                    .perform(get("/requests")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Test
    void testGetAllItemRequestByUserFailedNotHeaderUser() throws Exception {
        this.mockMvc
                .perform(get("/requests"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testGetAllItemRequestByUserFailedUserNotFound() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).getUserById(3L);

        this.mockMvc
                .perform(get("/requests")
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testGetItemRequest() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        ItemRequest itemRequest = Mockito.mock(ItemRequest.class);
        doReturn(Optional.of(itemRequest)).when(itemRequestService).getItemRequestById(2L);

        try (MockedStatic<ItemRequestMapper> utilities = Mockito.mockStatic(ItemRequestMapper.class)) {
            utilities.when(() -> ItemRequestMapper.toItemRequestDto(Mockito.mock(ItemRequest.class)))
                    .thenReturn(Mockito.mock(ItemRequestDto.class));

            this.mockMvc
                    .perform(get("/requests/2")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void testGetItemRequestFailedNoHeader() throws Exception {
        this.mockMvc
                .perform(get("/requests/2"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testGetItemRequestFailedUserNotFound() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).getUserById(3L);

        this.mockMvc
                .perform(get("/requests/2")
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    void testGetItemRequestFailedItemRequestNotFound() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        doThrow(ItemRequestNotFoundException.class).when(itemRequestService).getItemRequestById(2L);

        this.mockMvc
                .perform(get("/requests/2")
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testGetAllItemRequest() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        ItemRequest itemRequest = Mockito.mock(ItemRequest.class);
        doReturn(new PageImpl<>(List.of(itemRequest)))
                .when(itemRequestService)
                .getAllItemRequest(user, PageRequest.of(5, 4, Sort.by("created").descending()));

        try (MockedStatic<ItemRequestMapper> utilities = Mockito.mockStatic(ItemRequestMapper.class)) {
            utilities.when(() -> ItemRequestMapper.toItemRequestDto(Mockito.mock(ItemRequest.class)))
                    .thenReturn(Mockito.mock(ItemRequestDto.class));

            this.mockMvc
                    .perform(get("/requests/all")
                            .header("X-Sharer-User-Id", 3)
                            .param("from", "5")
                            .param("size", "4"))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void testCreatedItemRequest() throws Exception {
        String jsonContent = "{\"description\":\"description\"}";

        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        try (MockedStatic<ItemRequestMapper> utilities = Mockito.mockStatic(ItemRequestMapper.class)) {
            utilities.when(() -> ItemRequestMapper.toItemRequestDto(Mockito.mock(ItemRequest.class)))
                    .thenReturn(Mockito.mock(ItemRequestDto.class));

            this.mockMvc
                    .perform(post("/requests")
                            .header("X-Sharer-User-Id", 3)
                            .content(jsonContent)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }
}
