package ru.practicum.server.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.UserService;

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
        User user = mock(User.class);

        //When
        userService.createUser(user);

        //Then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        //Given
        Long idUser = 1L;
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());

        //When
        userService.deleteUser(idUser);

        //Then
        verify(userRepository, times(1)).delete(user);
    }

    @Test
     void testDeleteUserFailed() {
        //Given
        Long idUser = 1L;
        User user = mock(User.class);
        doThrow(new UserNotFoundException(idUser)).when(userRepository).findById(idUser);

        //When

        //Then
        catchThrowableOfType(() -> userService.deleteUser(idUser), UserNotFoundException.class);
        verify(userRepository, times(0)).delete(user);
    }

    @Test
    void testUpdateUser() {
        //Given
        User user = mock(User.class);
        doReturn(Optional.of(user)).when(userRepository).findById(1L);

        //When
        userService.updateUser(1L, Map.of("name", "name"));

        //Then
        verify(userRepository, times(1)).save(new User(0L, "name",null));
    }

    @Test
    void testUpdateUserFailed() {
        //Given
        Long idUser = 1L;
        User user = mock(User.class);
        doThrow(new UserNotFoundException(idUser)).when(userRepository).findById(idUser);

        //When

        //Then
        catchThrowableOfType(
                () -> userService.updateUser(1L, Map.of("name", "name")), UserNotFoundException.class
        );
        verify(userRepository, times(0)).delete(user);
    }

    @Test
    void testGetById() {
        //Given

        //When
        userService.getUserById(1L);

        //Then
        verify(userRepository, times(1)).findById(1L);
    }
}
