package ru.practicum.server.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.server.exception.*;

import java.util.List;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onUserNotFoundException(UserNotFoundException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMissingRequestHeaderException(MissingRequestHeaderException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(ItemNoBelongByUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onItemNoBelongByUserException(ItemNoBelongByUserException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onItemNotFoundException(ItemNotFoundException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation onIllegalArgumentException(IllegalArgumentException e) {
        return new Violation(e.getMessage());
    }

    @ExceptionHandler(ItemNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onItemNotAvailableException(ItemNotAvailableException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onBookingNotFoundException(BookingNotFoundException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(ItemBelongByUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onItemBelongByUserException(ItemBelongByUserException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(UserNotGiveItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onUserNotGiveItemException(UserNotGiveItemException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(ItemRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onItemRequestNotFoundException(ItemRequestNotFoundException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }
}
