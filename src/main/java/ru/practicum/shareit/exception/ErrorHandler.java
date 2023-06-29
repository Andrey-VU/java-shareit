package ru.practicum.shareit.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = {ValidationException.class, IncorrectItemDtoException.class,
            ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConversionFailedException(final RuntimeException e) {

        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS");
    }

    @ExceptionHandler(value = {UserNotFoundException.class, ItemNotFoundException.class,
            IncorrectIdException.class, BookingNotFoundException.class, ItemRequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }
}
