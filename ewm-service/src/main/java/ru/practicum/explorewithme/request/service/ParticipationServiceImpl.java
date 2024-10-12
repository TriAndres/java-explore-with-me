package ru.practicum.explorewithme.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.request.exception.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.dto.ParticipationRequestMapper;
import ru.practicum.explorewithme.request.model.ParticipationRequest;
import ru.practicum.explorewithme.request.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ParticipationServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ParticipationRequestDto> getUsersRequests(Long userId) {
        List<ParticipationRequest> userRequests = participationRequestRepository.findAllByRequesterId(userId);
        log.info("Возвращаю список запросов пользователя с id = " + userId);
        return ParticipationRequestMapper.listToParticipationRequestDto(userRequests);
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));

        List<ParticipationRequestDto> existedRequests = getUsersRequests(userId);
        for (ParticipationRequestDto participationRequestDto : existedRequests) {
            if (Objects.equals(participationRequestDto.getEvent(), eventId)) {
                throw new DuplicateRequestException("Запрос на участие в этом событии уже создан");
            }
        }
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new OwnerRequestException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new EventIsNotPublishedException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ParticipationLimitExceededException("Лимит участников превышен");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            ParticipationRequest participationRequest = new ParticipationRequest(null, LocalDateTime.now(), event, user, State.CONFIRMED);
            return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
        } else {
            ParticipationRequest participationRequest = new ParticipationRequest(null, LocalDateTime.now(), event, user, State.PENDING);
            return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
        }
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        ParticipationRequest request = participationRequestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException("Запроса с id = " + requestId + " не существует"));
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new OwnerRequestException("Пользователь с id =" + userId + " не является инициатором запроса с id = " + requestId);
        }
        request.setStatus(State.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByEventInitiatorIdAndEventId(userId, eventId);
        return ParticipationRequestMapper.listToParticipationRequestDto(participationRequests);
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipationRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllById(request.getRequestIds());
        if (!event.getRequestModeration()) {
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new ParticipationLimitExceededException("Лимит участников превышен");
            }
            for (ParticipationRequest participationRequest : participationRequests) {
                participationRequest.setStatus(State.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            }
            eventRepository.save(event);
            return new EventRequestStatusUpdateResult(ParticipationRequestMapper.listToParticipationRequestDto(participationRequests), new ArrayList<>());
        }

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();
        State status = State.valueOf(request.getStatus());

        for (ParticipationRequest participationRequest : participationRequests) {
            if (status == State.CONFIRMED) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    throw new ParticipationLimitExceededException("Лимит участников превышен");
                }
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                participationRequest.setStatus(State.CONFIRMED);
                confirmedRequests.add(participationRequest);
            } else {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    throw new ParticipationLimitExceededException("Лимит участников превышен");
                }
                participationRequest.setStatus(State.REJECTED);
                rejectedRequests.add(participationRequest);
            }
        }
        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(ParticipationRequestMapper.listToParticipationRequestDto(confirmedRequests), ParticipationRequestMapper.listToParticipationRequestDto(rejectedRequests));
    }
}
