package ru.practicum.shareit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DuplicationEmailException;
import ru.practicum.shareit.exception.ItemNoBelongByUserException;
import ru.practicum.shareit.exception.UserNotFoundException;

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
                .map(violation -> new Violation(violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .collect(Collectors.toList()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(DuplicationEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ValidationErrorResponse onDuplicationEmailException(DuplicationEmailException e) {
        final Violation violation = new Violation(e.getFieldName(), e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onUserNotFoundException(UserNotFoundException e) {
        final Violation violation = new Violation(e.getFieldName(), e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMissingRequestHeaderException(MissingRequestHeaderException e) {
        final Violation violation = new Violation("X-Sharer-User-Id", e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }

    @ExceptionHandler(ItemNoBelongByUserException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ValidationErrorResponse onMissingRequestHeaderException(ItemNoBelongByUserException e) {
        final Violation violation = new Violation(e.getFieldName(), e.getMessage());
        return new ValidationErrorResponse(List.of(violation));
    }
}
