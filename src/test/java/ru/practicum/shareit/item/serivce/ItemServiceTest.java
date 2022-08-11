package ru.practicum.shareit.item.serivce;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.ItemNoBelongByUserException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

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
        Item mock = Mockito.mock(Item.class);

        //When
        itemService.createItem(mock);

        //Then
        verify(repository, Mockito.times(1)).save(mock);
    }

    @Test
    void testUpdateItem() {
        //Given
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(4L);
        doReturn(4L).when(user).getId();

        Item item = Mockito.mock(Item.class);
        doReturn(Optional.of(item)).when(repository).findById(1L);

        User owner = Mockito.mock(User.class);
        doReturn(owner).when(item).getOwner();
        doReturn(4L).when(owner).getId();

        //When
        itemService.updateItem(1L, Map.of("name", "name"), 4L);

        //Then
        verify(repository, Mockito.times(1))
                .save(new Item(0L, "name", null, false, owner, null));

    }

    @Test
    void testUpdateItemFailed() {
        //Given
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(4L);
        doReturn(4L).when(user).getId();

        Item item = Mockito.mock(Item.class);
        doReturn(Optional.of(item)).when(repository).findById(1L);

        User owner = Mockito.mock(User.class);
        doReturn(owner).when(item).getOwner();
        doReturn(2L).when(owner).getId();

        //When

        //Then
        catchThrowableOfType(
                () -> itemService.updateItem(1L, Map.of("name", "name"), 4L),
                ItemNoBelongByUserException.class
        );
        verify(repository, Mockito.times(0))
                .save(new Item(0L, "name", null, false, owner, null));

    }

    @Test
    void testGetItemById() {
        //Given

        //When
        itemService.getItemById(1L);

        //Then
        verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testGetAllItemsByUser() {
        //Given
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(1L);

        //When
        itemService.getAllItemsByUser(1L, Pageable.unpaged());

        //Then
        verify(repository, Mockito.times(1)).findAllByOwnerEquals(user, Pageable.unpaged());
    }

    @Test
    void testDeleteItem() {
        //Given
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(4L);
        doReturn(4L).when(user).getId();

        Item item = Mockito.mock(Item.class);
        doReturn(Optional.of(item)).when(repository).findById(1L);

        User owner = Mockito.mock(User.class);
        doReturn(owner).when(item).getOwner();
        doReturn(4L).when(owner).getId();

        //When
        itemService.deleteItem(1L, 4L);

        //Then
        verify(repository, Mockito.times(1)).delete(item);
    }

    @Test
    void testDeleteItemFailed() {
        //Given
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(4L);
        doReturn(4L).when(user).getId();

        Item item = Mockito.mock(Item.class);
        doReturn(Optional.of(item)).when(repository).findById(1L);

        User owner = Mockito.mock(User.class);
        doReturn(owner).when(item).getOwner();
        doReturn(2L).when(owner).getId();

        //When

        //Then
        catchThrowableOfType(
                () -> itemService.deleteItem(1L, 4L),
                ItemNoBelongByUserException.class
        );
        verify(repository, Mockito.times(0)).delete(item);
    }

    @Test
    void testFindItemsByKeyword() {
        //Given
        String text = "text";
        Item item = Mockito.mock(Item.class);
        doReturn(List.of(item)).when(repository).findAllByText(text, Pageable.unpaged());

        //When
        itemService.findItemsByKeyword(text, Pageable.unpaged());

        //Then
        verify(repository, Mockito.times(1)).findAllByText(text, Pageable.unpaged());
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
