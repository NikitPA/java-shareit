package ru.practicum.server.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.server.request.ItemRequest;
import ru.practicum.server.request.ItemRequestRepository;
import ru.practicum.server.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class ItemRequestRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRequestRepository repository;

    @Test
    void testMethodsFindAllByRequestorOrderByCreatedDesc() {
        //Given
        User user = new User(null, "Nikita", "nik@mail.ru");
        ItemRequest itemRequest = new ItemRequest(null, "description", LocalDateTime.now(), user);
        em.persist(user);
        em.persist(itemRequest);

        //When
        List<ItemRequest> result = repository.findAllByRequestorOrderByCreatedDesc(user);

        //Then
        then(result).containsExactlyElementsOf(List.of(itemRequest));
    }

    @Test
    void testMethodsFindAllByRequestorIsNot() {
        //Given
        User user = new User(null, "Nikita", "nik@mail.ru");
        User userRequester = new User(null, "Nikita2", "nik2@mail.ru");
        ItemRequest itemRequest = new ItemRequest(null, "description", LocalDateTime.now(), userRequester);
        em.persist(userRequester);
        em.persist(user);
        em.persist(itemRequest);

        //When
        Page<ItemRequest> result = repository.findAllByRequestorIsNot(user, Pageable.unpaged());

        //Then
        then(result).containsExactlyElementsOf(List.of(itemRequest));
    }
}
