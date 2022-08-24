package ru.practicum.server.user;

import ru.practicum.server.user.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserDto user) {
        return new User(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User patchUser(User user, Map<Object, Object> updateFields) {
        return new User(
                user.getId(),
                checkName(user, updateFields),
                checkEmail(user, updateFields)
        );
    }

    private static String checkName(User user, Map<Object, Object> updateFields) {
        if (updateFields.containsKey("name")) {
            return updateFields.get("name").toString();
        } else {
            return user.getName();
        }
    }

    private static String checkEmail(User user, Map<Object, Object> updateFields) {
        if (updateFields.containsKey("email")) {
            return updateFields.get("email").toString();
        } else {
            return user.getEmail();
        }
    }
}
