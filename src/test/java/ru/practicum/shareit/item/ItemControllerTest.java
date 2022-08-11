package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetItems() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        doReturn(Collections.emptyList())
                .when(itemService)
                .getAllItemsByUser(1L, PageRequest.of(3, 3, Sort.by("id").ascending()));

        Item item = Mockito.mock(Item.class);
        Booking booking = Mockito.mock(Booking.class);
        doReturn(booking).when(bookingService).getNextBookingOfItem(item);
        doReturn(booking).when(bookingService).getLastBookingOfItem(item);

        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(get("/items")
                            .header("X-Sharer-User-Id", 3)
                            .param("from", "3")
                            .param("size", "3"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Test
    void testGetItemsFailedNotHeader() throws Exception {
        this.mockMvc
                .perform(get("/items")
                        .param("from", "3")
                        .param("size", "3"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

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
    void testGetItemByIdForUserIsOwner() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        Item item = Mockito.mock(Item.class);
        User owner = Mockito.mock(User.class);
        doReturn(Optional.of(item)).when(itemService).getItemById(1L);
        doReturn(owner).when(item).getOwner();
        doReturn(3L).when(owner).getId();

        Booking booking = Mockito.mock(Booking.class);
        doReturn(booking).when(bookingService).getNextBookingOfItem(item);
        doReturn(booking).when(bookingService).getLastBookingOfItem(item);

        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(get("/items/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
        verify(bookingService, Mockito.times(1)).getNextBookingOfItem(item);
        verify(bookingService, Mockito.times(1)).getLastBookingOfItem(item);
    }

    @Test
    void testGetItemByIdForUserIsNotOwner() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        Item item = Mockito.mock(Item.class);
        User owner = Mockito.mock(User.class);
        doReturn(Optional.of(item)).when(itemService).getItemById(1L);
        doReturn(owner).when(item).getOwner();
        doReturn(5L).when(owner).getId();

        Booking booking = Mockito.mock(Booking.class);
        doReturn(booking).when(bookingService).getNextBookingOfItem(item);
        doReturn(booking).when(bookingService).getLastBookingOfItem(item);

        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(get("/items/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
        verify(bookingService, Mockito.times(0)).getNextBookingOfItem(item);
        verify(bookingService, Mockito.times(0)).getLastBookingOfItem(item);
    }

    @Test
    void testGetItemByIdFailedNegativeUserId() throws Exception {
        this.mockMvc
                .perform(get("/items/-1")
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testGetItemByIdFailedNotHeader() throws Exception {
        this.mockMvc
                .perform(get("/items/1"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testGetItemByIdFailedItemNotFound() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        doThrow(ItemNotFoundException.class).when(itemService).getItemById(1L);

        this.mockMvc
                .perform(get("/items/1")
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testCreateItem() throws Exception {
        String json =
                "{\"name\":\"name\",\"description\":\"description\",\"available\":true}";

        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(post("/items")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isCreated())
                    .andDo(print());
        }
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
    void testCreateItemFailedNotHeader() throws Exception {
        String json =
                "{\"name\":\"name\",\"description\":\"description\",\"available\":true}";

        this.mockMvc
                .perform(post("/items")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.violations[0].error").isNotEmpty());
    }

    @Test
    void testUpdateItem() throws Exception {
        String json =
                "{\"name\":\"name\",\"description\":\"description\",\"available\":true}";

        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(patch("/items/1")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void testUpdateItemFailedPathVariableItemId() throws Exception {
        String json =
                "{\"name\":\"name\",\"description\":\"description\",\"available\":true}";

        this.mockMvc
                .perform(patch("/items/-1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testUpdateItemFailedNotHeader() throws Exception {
        String json =
                "{\"name\":\"name\",\"description\":\"description\",\"available\":true}";

        this.mockMvc
                .perform(patch("/items/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testUpdateItemFailedItemNotFound() throws Exception {
        String json = "{\"name\":\"name\"}";

        doThrow(ItemNotFoundException.class)
                .when(itemService)
                .updateItem(1L, Map.of("name", "name"), 3L);

        this.mockMvc
                .perform(patch("/items/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testDeleteItem() throws Exception {
        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(delete("/items/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void testDeleteItemFailedItemNotFound() throws Exception {
        doThrow(ItemNotFoundException.class)
                .when(itemService)
                .deleteItem(1L, 3L);

        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(delete("/items/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Test
    void testDeleteItemFailedUserNotFound() throws Exception {
        doThrow(UserNotFoundException.class)
                .when(itemService)
                .deleteItem(1L, 3L);

        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(delete("/items/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Test
    void testSearchAvailableItems() throws Exception {
        try (MockedStatic<ItemMapper> utilities = Mockito.mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(Mockito.mock(Item.class)))
                    .thenReturn(Mockito.mock(ItemDto.class));

            this.mockMvc
                    .perform(get("/items/search")
                            .param("text", "text")
                            .param("from", "3")
                            .param("size", "3"))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void testSearchAvailableItemsFailedNotParamText() throws Exception {
        this.mockMvc
                .perform(get("/items/search")
                        .param("from", "3")
                        .param("size", "3"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testCreateComment() throws Exception {
        String json = "{\"text\":\"text\"}";

        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        Item item = mock(Item.class);
        doReturn(Optional.of(item)).when(itemService).getItemById(1L);

        try (MockedStatic<CommentMapper> utilities = Mockito.mockStatic(CommentMapper.class)) {
            utilities.when(() -> CommentMapper.toCommentDto(Mockito.mock(Comment.class)))
                    .thenReturn(Mockito.mock(CommentDto.class));

            this.mockMvc
                    .perform(post("/items/1/comment")
                            .header("X-Sharer-User-Id", 3)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
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

    @Test
    void testCreateCommentFailedUserNotFound() throws Exception {
        String json = "{\"text\":\"text\"}";

        this.mockMvc
                .perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 3)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testCreateCommentFailedItemNotFound() throws Exception {
        String json = "{\"text\":\"text\"}";

        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        this.mockMvc
                .perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 3)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
