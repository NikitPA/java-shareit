package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.catchThrowableOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testCreateUser() {
        //Given
        User user = Mockito.mock(User.class);

        //When
        userService.createUser(user);

        //Then
        verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        //Given
        Long idUser = 1L;
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userRepository).findById(ArgumentMatchers.anyLong());

        //When
        userService.deleteUser(idUser);

        //Then
        verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
     void testDeleteUserFailed() {
        //Given
        Long idUser = 1L;
        User user = Mockito.mock(User.class);
        doThrow(new UserNotFoundException(idUser)).when(userRepository).findById(idUser);

        //When

        //Then
        catchThrowableOfType(() -> userService.deleteUser(idUser), UserNotFoundException.class);
        verify(userRepository, Mockito.times(0)).delete(user);
    }

    @Test
    void testUpdateUser() {
        //Given
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userRepository).findById(1L);

        //When
        userService.updateUser(1L, Map.of("name", "name"));

        //Then
        verify(userRepository, Mockito.times(1)).save(new User(0L, "name",null));
    }

    @Test
    void testUpdateUserFailed() {
        //Given
        Long idUser = 1L;
        User user = Mockito.mock(User.class);
        doThrow(new UserNotFoundException(idUser)).when(userRepository).findById(idUser);

        //When

        //Then
        catchThrowableOfType(
                () -> userService.updateUser(1L, Map.of("name", "name")), UserNotFoundException.class
        );
        verify(userRepository, Mockito.times(0)).delete(user);
    }

    @Test
    void testGetById() {
        //Given

        //When
        userService.getUserById(1L);

        //Then
        verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testGetAllUser() {
        //Given

        //When
        userService.getAllUsers();

        //Then
        verify(userRepository, Mockito.times(1)).findAll();
    }
}
