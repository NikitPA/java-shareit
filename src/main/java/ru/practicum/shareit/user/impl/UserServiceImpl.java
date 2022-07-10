package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicationEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public User getUserById(@NotNull Long id) {
        return userRepository.get(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь %d не найден", id)));
    }

    @Override
    public User createUser(@NotNull User user) {
        validate(user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(@NotNull Long id) {
        getUserById(id);
        userRepository.delete(id);
    }

    @Override
    public User updateUser(@NotNull Long id, @NotNull Map<Object, Object> updateFields) {
        User userById = getUserById(id);
        User user = UserMapper.patchUser(userById, updateFields);
        if (updateFields.get("email") != null) {
            validate(user);
        }
        return userRepository.save(user);
    }

    private void validate(@NotNull User user) {
        if (userRepository.getAll().stream()
                .filter(user1 -> user1.getEmail().equals(user.getEmail())).count() == 1) {
            throw new DuplicationEmailException(
                    String.format("Пользователь с электронной почтой %S существует", user.getEmail()));
        }
    }
}
