package ru.practicum.server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.user.dto.UserDto;

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
    private final String pathVariable = "userId";

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Collection<User> allUsers = userService.getAllUsers(from, size);
        List<UserDto> allUserDto = allUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allUserDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(pathVariable) long userId) {
        User userById = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return new ResponseEntity<>(UserMapper.toUserDto(userById), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(UserMapper.toUser(userDto));
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(pathVariable) long userId,
                                              @RequestBody Map<Object, Object> updateFields) {
        User user = userService.updateUser(userId, updateFields);
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(pathVariable) long userId) {
        userService.deleteUser(userId);
    }
}
