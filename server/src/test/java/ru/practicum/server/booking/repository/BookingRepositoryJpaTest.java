package ru.practicum.server.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.booking.Status;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class BookingRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void testMethodFindAllByItem_Owner() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.WAITING
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository.findAllByItem_Owner(user);

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByBooker() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.WAITING
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository.findAllByBooker(booker, Pageable.unpaged());

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByBookerAndStatus() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.WAITING
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByBookerAndStatus(booker, Status.WAITING, Pageable.unpaged());

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByBookerAndEndDateTimeBefore() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByBookerAndEndDateTimeBefore(booker,
                        start.plusDays(2),
                        Pageable.unpaged()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByBookerAndStartDateTimeAfter() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByBookerAndStartDateTimeAfter(booker,
                        start.minusDays(2),
                        Pageable.unpaged()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByBookerAndStartDateTimeBeforeAndEndDateTimeAfter() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByBookerAndStartDateTimeBeforeAndEndDateTimeAfter(booker,
                        start.plusDays(2),
                        start.minusDays(2),
                        Pageable.unpaged()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByItem_OwnerAndStatus() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByItem_OwnerAndStatus(user,
                        Status.APPROVED,
                        Pageable.unpaged()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByItem_OwnerAndEndDateTimeBefore() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByItem_OwnerAndEndDateTimeBefore(user,
                        start.plusDays(2),
                        Pageable.unpaged()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByItem_OwnerAndStartDateTimeAfter() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByItem_OwnerAndStartDateTimeAfter(user,
                        start.minusDays(2),
                        Pageable.unpaged()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByItem_OwnerAndStartDateTimeBeforeAndEndDateTimeAfter() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByItem_OwnerAndStartDateTimeBeforeAndEndDateTimeAfter(user,
                        start.plusDays(2),
                        start.minusDays(2),
                        Pageable.unpaged()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByItemAndStartDateTimeAfter() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByItemAndStartDateTimeAfter(item,
                        start.minusDays(2),
                        Sort.by("startDateTime").descending()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void testMethodFindAllByItemAndEndDateTimeBefore() {
        //Given
        LocalDateTime start = LocalDateTime.of(2022, 2, 3, 10, 10, 10);
        User user = new User(null, "Nikita", "nik@mail.ru");
        User booker = new User(null, "Nik", "n@mail.ru");
        Item item = new Item(null, "name", "description", true, user, null);
        Booking booking = new Booking(
                null, start, start.plusHours(1), item, booker, Status.APPROVED
        );
        em.persist(user);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        //When
        List<Booking> result = bookingRepository
                .findAllByItemAndEndDateTimeBefore(item,
                        start.plusDays(2),
                        Sort.by("startDateTime").descending()
                );

        //Then
        then(result).containsExactlyElementsOf(List.of(booking));
    }
}
