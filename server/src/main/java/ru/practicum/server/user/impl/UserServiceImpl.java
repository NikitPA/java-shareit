package ru.practicum.server.user.impl;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserMapper;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers(int from, int size) {
        int page = from / size;
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
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
