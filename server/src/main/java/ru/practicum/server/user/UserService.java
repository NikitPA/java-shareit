package ru.practicum.server.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers(int from, int size);

    Optional<User> getUserById(Long id);

    User createUser(User user);

    void deleteUser(Long id);

    User updateUser(Long id, Map<Object, Object> updateFields);
}
