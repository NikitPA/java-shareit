package ru.practicum.shareit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        return new ValidationErrorResponse(e.getConstraintViolations()
                .stream()
                .map(violation -> new Violation(violation.getMessage()))
                .collect(Collectors.toList()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

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
    public ValidationErrorResponse onIllegalArgumentException(IllegalArgumentException e) {
        final Violation violation = new Violation(e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
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
}
