package ru.practicum.main;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.main.exceptions.AlreadyPublishedException;
import ru.practicum.main.exceptions.CategoryIsNotEmptyException;
import ru.practicum.main.exceptions.CategoryNotExistException;
import ru.practicum.main.exceptions.CommentConflictException;
import ru.practicum.main.exceptions.CommentNotExistException;
import ru.practicum.main.exceptions.CompilationNotExistException;
import ru.practicum.main.exceptions.EventAlreadyCanceledException;
import ru.practicum.main.exceptions.EventIsNotPublishedException;
import ru.practicum.main.exceptions.EventNotExistException;
import ru.practicum.main.exceptions.NameAlreadyExistException;
import ru.practicum.main.exceptions.ParticipantLimitException;
import ru.practicum.main.exceptions.RequestAlreadyConfirmedException;
import ru.practicum.main.exceptions.RequestAlreadyExistException;
import ru.practicum.main.exceptions.RequestNotExistException;
import ru.practicum.main.exceptions.UserNotExistException;
import ru.practicum.main.exceptions.WrongTimeException;
import ru.practicum.main.exceptions.WrongUserException;
import ru.practicum.main.models.ApiError;
import ru.practicum.main.constants.Pattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Pattern.DATE);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleUserNameAlreadyExistException(final NameAlreadyExistException exception) {
        return new ApiError(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleUserNameAlreadyExistException(final CommentConflictException exception) {
        return new ApiError(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleRequestAlreadyExistException(final RequestAlreadyExistException exception) {
        return new ApiError(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleCategoryIsNotEmptyException(final CategoryIsNotEmptyException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleEventIsNotPublishedException(final EventIsNotPublishedException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleWrongUserException(final WrongUserException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleAlreadyPublishedException(final AlreadyPublishedException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleParticipantLimitException(final ParticipantLimitException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleEventAlreadyCanceledException(final EventAlreadyCanceledException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleRequestAlreadyConfirmedException(final RequestAlreadyConfirmedException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleWrongTimeOfEventException(final WrongTimeException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseBody
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return new ApiError(exception.getMessage(), "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new ApiError(exception.getMessage(), "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleUserNotExistException(final UserNotExistException exception) {
        return new ApiError("Can't delete user with this id", "User with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {
        return new ApiError("It is impossible to do the operation", "data not found",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleCompilationNotExistException(final CompilationNotExistException exception) {
        return new ApiError("Can't delete user with this id", "User with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleRequestNotExistException(final RequestNotExistException exception) {
        return new ApiError(exception.getMessage(), "Request with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEventNotExistException(final EventNotExistException exception) {
        return new ApiError(exception.getMessage(), "Event with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotExistException(final CategoryNotExistException exception) {
        return new ApiError("Can't delete category with this id", "Category with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCommentNotExistException(final CommentNotExistException exception) {
        return new ApiError(exception.getMessage(), "Category with this id doesn't exist",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }
}
