package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ViolationException;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.Status;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.requests.model.Status.CANCELED;
import static ru.practicum.requests.model.Status.CONFIRMED;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        if (!requestRepository.findAllByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new ViolationException("Нельзя добавить повторный запрос");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено."));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ViolationException("Нельзя участвовать в неопубликованном событии");
        }

        if (event.getParticipantLimit() != 0 &&
                requestRepository.countByEventIdAndStatus(eventId, CONFIRMED) >= event.getParticipantLimit()) {
            throw new ViolationException("У события достигнут лимит запросов на участие");
        }

        if (userId.equals(event.getInitiator().getId())) {
            throw new ViolationException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);
        request.setEvent(event);
        request.setStatus(Status.PENDING);

        Status status;
        if (event.getRequestModeration()) {
            status = Status.PENDING;
        } else {
            status = CONFIRMED;
        }
        if (event.getParticipantLimit() == 0) {
            request.setStatus(CONFIRMED);
        } else {
            request.setStatus(status);
        }
        Request savedRequest = requestRepository.save(request);

        return requestMapper.requestToParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> getAllRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));

        List<Request> requests = requestRepository.findAllByRequesterId(userId);

        if (requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream().map(requestMapper::requestToParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id: " + requestId + " не найден"));
        request.setStatus(CANCELED);
        return requestMapper.requestToParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено."));

        List<Request> requests = requestRepository.findAllByEvent(event).stream()
                .filter(request -> request.getEvent().getInitiator().getId().equals(userId))
                .collect(Collectors.toList());

        if (requests.isEmpty()) {
            return new ArrayList<>();
        }
        return requests.stream()
                .map(requestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено."));

        List<Request> requestsToUpdate = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());

        if (requestsToUpdate.stream()
                .anyMatch(request -> !request.getStatus().equals(Status.PENDING))) {
            throw new ViolationException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        if (eventRequestStatusUpdateRequest.getStatus().equals(Status.REJECTED)) {
            rejectedRequests = requestsToUpdate.stream()
                    .map(request -> {
                        request.setStatus(Status.REJECTED);
                        return requestMapper.requestToParticipationRequestDto(requestRepository.save(request));
                    }).collect(Collectors.toList());
        }

        if (eventRequestStatusUpdateRequest.getStatus().equals(CONFIRMED)) {
            if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                confirmedRequests = requestsToUpdate.stream()
                        .map(request -> {
                            request.setStatus(CONFIRMED);
                            return requestMapper.requestToParticipationRequestDto(requestRepository.save(request));
                        }).collect(Collectors.toList());
            } else if (requestRepository.countByEventIdAndStatus(eventId, CONFIRMED) >= event.getParticipantLimit()) {
                throw new ViolationException("Нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие");
            }

            for (Request request : requestsToUpdate) {
                if (requestRepository.countByEventIdAndStatus(eventId, CONFIRMED) < event.getParticipantLimit()) {
                    request.setStatus(CONFIRMED);
                    confirmedRequests.add(requestMapper.requestToParticipationRequestDto(request));
                } else {
                    request.setStatus(Status.REJECTED);
                    rejectedRequests.add(requestMapper.requestToParticipationRequestDto(request));
                }
                requestRepository.save(request);
            }
        }

        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
