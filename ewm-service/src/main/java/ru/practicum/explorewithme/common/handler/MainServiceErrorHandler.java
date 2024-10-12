package ru.practicum.explorewithme.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.comment.exception.CommentNotFoundException;
import ru.practicum.explorewithme.comment.exception.NotAuthorException;
import ru.practicum.explorewithme.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.event.exception.*;
import ru.practicum.explorewithme.request.exception.*;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class MainServiceErrorHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({CategoryNotFoundException.class, UserNotFoundException.class, EventNotFoundException.class, RequestNotFoundException.class, CompilationNotFoundException.class, CommentNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final RuntimeException e) {
        return new ApiError(HttpStatus.NOT_FOUND.toString(), e.getClass().toString(), e.getLocalizedMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler({ParticipationLimitExceededException.class, EventIsAlreadyPublishedException.class, EventIsNotPendingException.class, DuplicateRequestException.class,
    OwnerRequestException.class, EventIsNotPublishedException.class, DataIntegrityViolationException.class, NotAuthorException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final RuntimeException e) {
        return new ApiError(HttpStatus.NOT_FOUND.toString(), e.getClass().toString(), e.getLocalizedMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler({NotValidException.class, EventIsTooSoonException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final RuntimeException e) {
        return new ApiError(HttpStatus.NOT_FOUND.toString(), e.getClass().toString(), e.getLocalizedMessage(), LocalDateTime.now().format(FORMATTER));
    }
}