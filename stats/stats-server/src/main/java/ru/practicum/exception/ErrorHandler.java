package ru.practicum.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handler(HttpServletRequest request, final Exception e) {
        log.error("Requested URL= {}", request.getRequestURL());
        log.error("BAD_REQUEST {}", e.getMessage());
        return ErrorResponse.builder()
                .status("NOT_FOUND")
                .reason("The required object was not found.")
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final ValidationException e) {
        log.error("BAD_REQUEST {}", e.getMessage());
        return ErrorResponse.builder()
                .status("FORBIDDEN")
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
    }
}
