package ru.practicum.explorewithme.event.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.common.util.PageCreatorUtil;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.exception.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.Location;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.model.StateAction;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.repository.LocationRepository;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class EventServiceImpl implements EventService {

    private final StatsClient statsClient;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        if (LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventIsTooSoonException("Нельзя запланировать мероприятие меньше чем за 2 часа до его начала");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Категории с id = " + newEventDto.getCategory() + " не существует"));
        Location existedLocation = locationRepository.findByLatAndLon(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon());
        if (existedLocation == null) {
            existedLocation = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        }
        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, user, category, existedLocation));
        log.info("Событие: " + event + " сохранено");
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> findEventsByCurrentUser(Long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        PageRequest page = PageCreatorUtil.createPage(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, page);
        log.info("Найдены события: " + events);
        return EventMapper.listToEventShortDto(events);
    }

    @Override
    public EventFullDto findEventByCurrentUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        log.info("Найдено событие: " + event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotInitiatorException("Пользователь с id = " + userId + " не может обновлять событие с id = " + eventId);
        }
        if (event.getState() == State.PUBLISHED) {
            throw new EventIsAlreadyPublishedException("Опубликованное событие нельзя изменять");
        }
        if (request.getEventDate() != null) {
            if (LocalDateTime.parse(request.getEventDate(), FORMATTER).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new EventIsTooSoonException("Нельзя изменять мероприятие меньше чем за 2 часа до его начала");
            }
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            event.setCategory(categoryRepository.findById(request.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Категории с id = " + request.getCategory() + " не существует")));
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), FORMATTER));
        }
        if (request.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(request.getLocation()));
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(request.getStateAction());
            if (stateAction == StateAction.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
            } else if (stateAction == StateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            } else if (stateAction == StateAction.PUBLISH_EVENT) {
                event.setState(State.PUBLISHED);
            } else {
                event.setState(State.CANCELED);
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        Event updatedEvent = eventRepository.save(event);
        log.info("Событие: " + event + "обновлено");
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> findEventsPublic(String textParameter, List<Long> categoriesId, Boolean paid, String rangeStartParameter,
                                                String rangeEndParameter, Boolean onlyAvailable, String sortParameter, int from, int size,
                                                HttpServletRequest request) {
        String text = null;
        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;
        String sort;
        PageRequest page;

        if (textParameter != null) {
            text = textParameter.toLowerCase();
        }
        if (rangeStartParameter != null) {
            rangeStart = LocalDateTime.parse(rangeStartParameter, FORMATTER);
        } else {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEndParameter != null) {
            rangeEnd = LocalDateTime.parse(rangeEndParameter, FORMATTER);
            if (rangeEnd.isBefore(rangeStart)) {
                throw new NotValidException("Окончание мероприятия не может быть раньше его начала");
            }
        } else {
            rangeEnd = LocalDateTime.now().plusMonths(3);
        }
        if (sortParameter != null) {
            if (sortParameter.equals("EVENT_DATE")) {
                sort = "eventDate";
            } else {
                sort = "views";
            }
            page = PageCreatorUtil.createPage(from, size, sort);
        } else {
            page = PageCreatorUtil.createPage(from, size);
        }

        List<Category> categories;
        if (categoriesId != null) {
            categories = categoryRepository.findAllById(categoriesId);
            if (categories.isEmpty()) {
                categories = null;
            }
        } else {
            categories = null;
        }

        statsClient.addHit(new EndpointHitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(FORMATTER)));

        List<Event> events;
        if (text != null && paid != null && categories != null && onlyAvailable) {
            events = eventRepository.findAllOnlyAvailable(text, paid, rangeStart, rangeEnd, categories, page);
        } else if (text != null && paid != null && onlyAvailable) {
            events = eventRepository.findAllOnlyAvailable(text, paid, rangeStart, rangeEnd, page);
        } else if (text != null && categories != null && onlyAvailable) {
            events = eventRepository.findAllOnlyAvailable(text, rangeStart, rangeEnd, categories, page);
        } else if (paid != null && categories != null && onlyAvailable) {
            events = eventRepository.findAllOnlyAvailable(paid, rangeStart, rangeEnd, categories, page);
        } else if (text != null && onlyAvailable) {
            events = eventRepository.findAllOnlyAvailable(text, rangeStart, rangeEnd, page);
        } else if (paid != null && onlyAvailable) {
            events = eventRepository.findAllOnlyAvailable(paid, rangeStart, rangeEnd, page);
        } else if (categories != null && onlyAvailable) {
            events = eventRepository.findAllOnlyAvailable(rangeStart, rangeEnd, categories, page);
        } else if (onlyAvailable) {
            events = eventRepository.findAllOnlyAvailable(rangeStart, rangeEnd, page);
        } else if (text != null && paid != null && categories != null) {
            events = eventRepository.findAll(text, paid, rangeStart, rangeEnd, categories, page);
        } else if (text != null && paid != null) {
            events = eventRepository.findAll(text, paid, rangeStart, rangeEnd, page);
        } else if (text != null && categories != null) {
            events = eventRepository.findAll(text, rangeStart, rangeEnd, categories, page);
        } else if (paid != null && categories != null) {
            events = eventRepository.findAll(paid, rangeStart, rangeEnd, categories, page);
        } else if (text != null) {
            events = eventRepository.findAll(text, rangeStart, rangeEnd, page);
        } else if (paid != null) {
            events = eventRepository.findAll(paid, rangeStart, rangeEnd, page);
        } else if (categories != null) {
            events = eventRepository.findAll(rangeStart, rangeEnd, categories, page);
        } else {
            events = eventRepository.findAll(rangeStart, rangeEnd, page);
        }

        if (events == null) {
            events = new ArrayList<>();
        }

        log.info("События по указаным параметрам найдены");
        return EventMapper.listToEventShortDto(events);
    }

    @Override
    public EventFullDto findEventPublic(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("События с id = " + id + " не существует"));
        if (event.getState() != State.PUBLISHED) {
            throw new EventNotFoundException("Событие с id = " + id + " еще на модерации");
        }

        Boolean unique = statsClient.checkUnique(request.getRequestURI(), request.getRemoteAddr());
        if (unique) {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }

        statsClient.addHit(new EndpointHitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(FORMATTER)));
        log.info("Возвращаю найденное событие");
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> findEventsAdmin(List<Long> usersIds, List<String> states, List<Long> categoriesIds, String rangeStartParameter, String rangeEndParameter, int from, int size) {
        List<User> users;
        if (usersIds != null) {
            users = userRepository.findAllById(usersIds);
        } else {
            users = null;
        }

        List<State> statesEnum;
        if (states != null) {
            statesEnum = states.stream().map(State::valueOf).collect(Collectors.toList());
        } else {
            statesEnum = null;
        }

        List<Category> categories;
        if (categoriesIds != null) {
            categories = categoryRepository.findAllById(categoriesIds);
            if (categories.isEmpty()) {
                categories = null;
            }
        } else {
            categories = null;
        }

        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;
        if (rangeStartParameter != null) {
            rangeStart = LocalDateTime.parse(rangeStartParameter, FORMATTER);
        } else {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEndParameter != null) {
            rangeEnd = LocalDateTime.parse(rangeEndParameter, FORMATTER);
            if (rangeEnd.isBefore(rangeStart)) {
                throw new NotValidException("Окончание мероприятия не может быть раньше его начала");
            }
        } else {
            rangeEnd = LocalDateTime.now().plusMonths(3);
        }

        PageRequest page = PageCreatorUtil.createPage(from, size);

        List<Event> events;
        if (users != null && statesEnum != null && categories != null) {
            events = eventRepository.findAllByInitiatorInAndStateInAndCategoryInAndEventDateIsAfterAndEventDateIsBefore(users,
                    statesEnum, categories, rangeStart, rangeEnd, page);
        } else if (users != null && statesEnum != null) {
            events = eventRepository.findAllByInitiatorInAndStateInAndEventDateIsAfterAndEventDateIsBefore(users, statesEnum,
                    rangeStart, rangeEnd, page);
        } else if (users != null && categories != null) {
            events = eventRepository.findAllByInitiatorInAndCategoryInAndEventDateIsAfterAndEventDateIsBefore(users, categories,
                    rangeStart, rangeEnd, page);
        } else if (statesEnum != null && categories != null) {
            events = eventRepository.findAllByStateInAndCategoryInAndEventDateIsAfterAndEventDateIsBefore(statesEnum,
                    categories, rangeStart, rangeEnd, page);
        } else if (users != null) {
            events = eventRepository.findAllByInitiatorInAndEventDateIsAfterAndEventDateIsBefore(users, rangeStart, rangeEnd, page);
        } else if (statesEnum != null) {
            events = eventRepository.findAllByStateInAndEventDateIsAfterAndEventDateIsBefore(statesEnum, rangeStart, rangeEnd, page);
        } else  if (categories != null) {
            events = eventRepository.findAllByCategoryInAndEventDateIsAfterAndEventDateIsBefore(categories, rangeStart, rangeEnd, page);
        } else {
            events = eventRepository.findAllByEventDateIsAfterAndEventDateIsBefore(rangeStart, rangeEnd, page);
        }

        if (events == null) {
            events = new ArrayList<>();
        }

        log.info("События по указаным параметрам найдены");
        return EventMapper.listToEventFullDto(events);
    }

    @Override
    public EventFullDto patchEventAdmin(Long eventId, UpdateEventAdminRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        if (request.getEventDate() != null) {
            if (LocalDateTime.parse(request.getEventDate(), FORMATTER).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new EventIsTooSoonException("Нельзя изменять мероприятие меньше чем за 2 часа до его начала");
            }
        }
        if (request.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(request.getStateAction());
            if (stateAction == StateAction.PUBLISH_EVENT) {
                if (event.getState() != State.PENDING) {
                    throw new EventIsNotPendingException("Нельзя опубликовать событие, если его статус не PENDING");
                }
            }
            if (stateAction == StateAction.REJECT_EVENT) {
                if (event.getState() == State.PUBLISHED) {
                    throw new EventIsNotPendingException("Нельзя отклонить опубликованное событие");
                }
            }
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            event.setCategory(categoryRepository.findById(request.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Категории с id = " + request.getCategory() + " не существует")));
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), FORMATTER));
        }
        if (request.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(request.getLocation()));
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(request.getStateAction());
            if (stateAction == StateAction.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
            } else if (stateAction == StateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            } else if (stateAction == StateAction.PUBLISH_EVENT) {
                event.setState(State.PUBLISHED);
            } else {
                event.setState(State.CANCELED);
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        Event updatedEvent = eventRepository.save(event);
        log.info("Событие: " + event + "обновлено");
        return EventMapper.toEventFullDto(event);
    }
}
