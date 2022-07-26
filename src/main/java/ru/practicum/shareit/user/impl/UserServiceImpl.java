package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(@NotNull Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(@NotNull User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(@NotNull Long id) {
        User userById = getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(userById);
    }

    @Override
    public User updateUser(@NotNull Long id, @NotNull Map<Object, Object> updateFields) {
        User userById = getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
        User user = UserMapper.patchUser(userById, updateFields);
        return userRepository.save(user);
    }
}
