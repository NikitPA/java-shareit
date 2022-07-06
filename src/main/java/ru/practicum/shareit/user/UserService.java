package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Map;

public interface UserService {

    Collection<User> getAllUsers();

    User getUserById(Long id);

    User createUser(User user);

    void deleteUser(Long id);

    User updateUser(Long id, Map<Object, Object> updateFields);
}
