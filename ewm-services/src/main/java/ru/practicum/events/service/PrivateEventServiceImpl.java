package ru.practicum.events.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateUser;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.AccessException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.events.model.State.*;

@Slf4j
@Service
public class PrivateEventServiceImpl extends EventBase implements PrivateEventService {
    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;

    private final UserRepository userRepository;

    public PrivateEventServiceImpl(EventRepository eventRepository,
                                   CategoryRepository categoryRepository,
                                   UserRepository userRepository,
                                   LocationRepository locationRepository,
                                   EventMapper eventMapper,
                                   LocationMapper locationMapper,
                                   RequestRepository requestRepository,
                                   StatsClient statsClient) {
        super(requestRepository, statsClient);
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.eventMapper = eventMapper;
        this.locationMapper = locationMapper;
    }

    @Override
    public EventDto addEvent(Long userId, NewEventDto eventDto) {
        Long categoryId = eventDto.getCategory();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id: " + categoryId + " не найдена"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        Location location = locationRepository.save(locationMapper.locationDtoToLocation(eventDto.getLocation()));
        Event event = eventMapper.newEventDtoToEvent(eventDto, category, user, location);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(PENDING);
        Event newEvent = eventRepository.save(event);
        return eventMapper.eventToEventDto(newEvent, null, null);
    }

    @Override
    public EventDto updateEvent(Long userId, Long eventId, EventUpdateUser eventUpdateUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));
        if (!event.getInitiator().equals(user)) {
            throw new AccessException("No access");
        }
        if (event.getState().equals(PUBLISHED)) {
            throw new AccessException("Событие должно быть PENDING или CANCELED");
        }
        if (eventUpdateUser.getStateAction() != null) {
            switch (eventUpdateUser.getStateAction()) {
                case "SEND_TO_REVIEW":
                    event.setState(PENDING);
                    break;
                case "CANCEL_REVIEW":
                    event.setState(CANCELED);
                    break;
                default:
                    throw new NotFoundException("Неизвестное состояние: " + eventUpdateUser.getStateAction());
            }
        }
        if (eventUpdateUser.getCategoryId() != null) {
            Long categoryId = eventUpdateUser.getCategoryId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Пользователь с id: " + categoryId + " не найдена"));
            event.setCategory(category);
        }
        Event updatedEvent = eventMapper.eventToEventUpdateUser(event, eventUpdateUser);
        event = eventRepository.save(updatedEvent);
        List<Event> eventList = List.of(event);
        Map<Long, Long> confirmedRequests = getConfirmedRequests(eventList);
        Map<Long, Long> viewStats = getViewsForEvents(eventList);
        return eventMapper.eventToEventDto(event, confirmedRequests.getOrDefault(eventId, 0L),
                viewStats.getOrDefault(eventId, 0L));
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, PageRequest pageRequest) {
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);
        if (events == null) {
            return List.of();
        }
        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Long, Long> viewStats = getViewsForEvents(events);
        log.info("Лист contents: {}", events.stream()
                .map(Event::toString)
                .collect(Collectors.joining(", ")));
        return eventMapper.eventListToEventShortDtoList(events, viewStats, confirmedRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEventByUserAndEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));
        List<Event> eventList = List.of(event);
        Map<Long, Long> confirmedRequests = getConfirmedRequests(eventList);
        Map<Long, Long> viewStats = getViewsForEvents(eventList);
        return eventMapper.eventToEventDto(event, confirmedRequests.getOrDefault(eventId, 0L),
                viewStats.getOrDefault(eventId, 0L));
    }


}
