package ru.practicum.server.item.serivce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.practicum.server.exception.ItemNoBelongByUserException;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.item.ItemService;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository repository;

    @MockBean
    private UserService userService;

    @Test
    void testCreateItem() {
        //Given
        Item mock = mock(Item.class);

        //When
        itemService.createItem(mock);

        //Then
        verify(repository, times(1)).save(mock);
    }

    @Test
    void testUpdateItem() {
        //Given
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(4L);
        doReturn(4L).when(user).getId();

        Item item = mock(Item.class);
        doReturn(Optional.of(item)).when(repository).findById(1L);

        User owner = mock(User.class);
        doReturn(owner).when(item).getOwner();
        doReturn(4L).when(owner).getId();

        //When
        itemService.updateItem(1L, Map.of("name", "name"), 4L);

        //Then
        verify(repository, times(1))
                .save(new Item(0L, "name", null, false, owner, null));

    }

    @Test
    void testUpdateItemFailed() {
        //Given
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(4L);
        doReturn(4L).when(user).getId();

        Item item = mock(Item.class);
        doReturn(Optional.of(item)).when(repository).findById(1L);

        User owner = mock(User.class);
        doReturn(owner).when(item).getOwner();
        doReturn(2L).when(owner).getId();

        //When

        //Then
        catchThrowableOfType(
                () -> itemService.updateItem(1L, Map.of("name", "name"), 4L),
                ItemNoBelongByUserException.class
        );
        verify(repository, times(0))
                .save(new Item(0L, "name", null, false, owner, null));

    }

    @Test
    void testGetItemById() {
        //Given

        //When
        itemService.getItemById(1L);

        //Then
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetAllItemsByUser() {
        //Given
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(1L);

        //When
        itemService.getAllItemsByUser(1L, Pageable.unpaged());

        //Then
        verify(repository, times(1)).findAllByOwnerEquals(user, Pageable.unpaged());
    }

    @Test
    void testDeleteItem() {
        //Given
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(4L);
        doReturn(4L).when(user).getId();

        Item item = mock(Item.class);
        doReturn(Optional.of(item)).when(repository).findById(1L);

        User owner = mock(User.class);
        doReturn(owner).when(item).getOwner();
        doReturn(4L).when(owner).getId();

        //When
        itemService.deleteItem(1L, 4L);

        //Then
        verify(repository, times(1)).delete(item);
    }

    @Test
    void testDeleteItemFailed() {
        //Given
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(4L);
        doReturn(4L).when(user).getId();

        Item item = mock(Item.class);
        doReturn(Optional.of(item)).when(repository).findById(1L);

        User owner = mock(User.class);
        doReturn(owner).when(item).getOwner();
        doReturn(2L).when(owner).getId();

        //When

        //Then
        catchThrowableOfType(
                () -> itemService.deleteItem(1L, 4L),
                ItemNoBelongByUserException.class
        );
        verify(repository, times(0)).delete(item);
    }

    @Test
    void testFindItemsByKeyword() {
        //Given
        String text = "text";
        Item item = mock(Item.class);
        doReturn(List.of(item)).when(repository).findAllByText(text, Pageable.unpaged());

        //When
        itemService.findItemsByKeyword(text, Pageable.unpaged());

        //Then
        verify(repository, times(1)).findAllByText(text, Pageable.unpaged());
    }

    @Test
    void testFindItemsByKeywordEmpty() {
        //Given
        String text = "";

        //When
        List<Item> result = itemService.findItemsByKeyword(text, Pageable.unpaged());

        //Then
        then(result).isEmpty();
        verifyNoInteractions(repository);
    }
}
