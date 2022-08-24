package ru.practicum.server.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.user.User;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class ItemRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testMethodFindAllByOwnerEquals() {
        //Given
        User user = new User(null, "Nikita", "nik@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        em.persist(user);
        em.persist(item);

        //When
        List<Item> result = itemRepository.findAllByOwnerEquals(user, Pageable.unpaged());

        //Then
        then(result).containsExactlyElementsOf(List.of(item));
    }

    @Test
    void testMethodFindAllByText() {
        //Given
        User user = new User(null, "Nikita", "nik@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        em.persist(user);
        em.persist(item);

        //When
        List<Item> result = itemRepository.findAllByText("naMe", Pageable.unpaged());

        //Then
        then(result).containsExactlyElementsOf(List.of(item));
    }
}
