package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers() {
        Collection<User> allUsers = userService.getAllUsers();
        List<UserDto> allUserDto = allUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allUserDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") @Min(1) long userId) {
        User userById = userService.getUserById(userId);
        return new ResponseEntity<>(UserMapper.toUserDto(userById), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.createUser(UserMapper.toUser(userDto));
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") long userId,
                                              @RequestBody Map<Object, Object> updateFields) {
        User user = userService.updateUser(userId, updateFields);
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("valid", HttpStatus.OK);
    }
}
