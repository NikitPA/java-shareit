package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> getAll();

    Optional<User> get(Long id);

    User save(User user);

    void delete(Long id);
}
