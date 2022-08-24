package ru.practicum.server.item;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingService;
import ru.practicum.server.comment.Comment;
import ru.practicum.server.comment.CommentDto;
import ru.practicum.server.comment.CommentMapper;
import ru.practicum.server.comment.CommentService;
import ru.practicum.server.exception.ItemNotFoundException;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.request.ItemRequestService;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserService;

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
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        doReturn(Collections.emptyList())
                .when(itemService)
                .getAllItemsByUser(1L, PageRequest.of(3, 3, Sort.by("id").ascending()));

        Item item = mock(Item.class);
        Booking booking = mock(Booking.class);
        doReturn(booking).when(bookingService).getNextBookingOfItem(item);
        doReturn(booking).when(bookingService).getLastBookingOfItem(item);

        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

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
    void testGetItemByIdForUserIsOwner() throws Exception {
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        Item item = mock(Item.class);
        User owner = mock(User.class);
        doReturn(Optional.of(item)).when(itemService).getItemById(1L);
        doReturn(owner).when(item).getOwner();
        doReturn(3L).when(owner).getId();

        Booking booking = mock(Booking.class);
        doReturn(booking).when(bookingService).getNextBookingOfItem(item);
        doReturn(booking).when(bookingService).getLastBookingOfItem(item);

        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

            this.mockMvc
                    .perform(get("/items/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
        verify(bookingService, times(1)).getNextBookingOfItem(item);
        verify(bookingService, times(1)).getLastBookingOfItem(item);
    }

    @Test
    void testGetItemByIdForUserIsNotOwner() throws Exception {
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        Item item = mock(Item.class);
        User owner = mock(User.class);
        doReturn(Optional.of(item)).when(itemService).getItemById(1L);
        doReturn(owner).when(item).getOwner();
        doReturn(5L).when(owner).getId();

        Booking booking = mock(Booking.class);
        doReturn(booking).when(bookingService).getNextBookingOfItem(item);
        doReturn(booking).when(bookingService).getLastBookingOfItem(item);

        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

            this.mockMvc
                    .perform(get("/items/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
        verify(bookingService, times(0)).getNextBookingOfItem(item);
        verify(bookingService, times(0)).getLastBookingOfItem(item);
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
        User user = mock(User.class);
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

        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

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

        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

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
        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

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

        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

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

        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

            this.mockMvc
                    .perform(delete("/items/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Test
    void testSearchAvailableItems() throws Exception {
        try (MockedStatic<ItemMapper> utilities = mockStatic(ItemMapper.class)) {
            utilities.when(() -> ItemMapper.toItemDto(mock(Item.class)))
                    .thenReturn(mock(ItemDto.class));

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

        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        Item item = mock(Item.class);
        doReturn(Optional.of(item)).when(itemService).getItemById(1L);

        try (MockedStatic<CommentMapper> utilities = mockStatic(CommentMapper.class)) {
            utilities.when(() -> CommentMapper.toCommentDto(mock(Comment.class)))
                    .thenReturn(mock(CommentDto.class));

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

        User user = mock(User.class);
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
