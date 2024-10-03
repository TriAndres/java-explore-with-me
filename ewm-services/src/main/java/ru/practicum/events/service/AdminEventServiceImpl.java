package ru.practicum.events.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventUpdateAdmin;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.AdminEventParams;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.ViolationException;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.requests.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.events.model.State.*;

@Service
public class AdminEventServiceImpl extends EventBase implements AdminEventService {
    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    private final EventMapper eventMapper;

    public AdminEventServiceImpl(EventRepository eventRepository,
                                 CategoryRepository categoryRepository,
                                 LocationRepository locationRepository,
                                 EventMapper eventMapper,
                                 RequestRepository requestRepository,
                                 StatsClient statsClient,
                                 LocationMapper locationMapper) {
        super(requestRepository, statsClient);
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.eventMapper = eventMapper;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<EventDto> getAllAdminEvents(AdminEventParams eventParams, PageRequest pageRequest) {
        List<Specification<Event>> specifications = new ArrayList<>();
        if (!eventParams.getStates().isEmpty()) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state"))
                    .value(eventParams.getStates()));
        }
        if (!eventParams.getUserIds().isEmpty()) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id"))
                    .value(eventParams.getUserIds()));
        }
        if (!eventParams.getCategoriesIds().isEmpty()) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id"))
                    .value(eventParams.getCategoriesIds()));
        }
        if (eventParams.getStart() != null) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                    .greaterThanOrEqualTo(root.get("eventDate"), eventParams.getStart()));
        }
        if (eventParams.getEnd() != null) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                    .lessThanOrEqualTo(root.get("eventDate"), eventParams.getEnd()));
        }

        List<Event> events = eventRepository.findAll(specifications
                .stream()
                .reduce(Specification::and)
                .orElse(null), pageRequest).toList();
        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Long, Long> viewStats = getViewsForEvents(events);

        return events.stream()
                .map(event -> eventMapper.eventToEventDto(
                        event,
                        confirmedRequests.getOrDefault(event.getId(), 0L),
                        viewStats.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updateEvent(Long eventId, EventUpdateAdmin eventUpdateAdmin) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено."));

        if (!event.getState().equals(PENDING)) {
            throw new ViolationException("Дата события не может быть изменена.");
        }

        if (LocalDateTime.now().isAfter(event.getEventDate())) {
            throw new ValidationException("Ошибка даты.");
        }

        updateEventFields(event, eventUpdateAdmin);
        updateEventState(event, eventUpdateAdmin.getStateAction());

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.eventToEventDto(updatedEvent, 0L, 0L);
    }

    private void updateEventFields(Event event, EventUpdateAdmin eventUpdateAdmin) {
        Optional.ofNullable(eventUpdateAdmin.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventUpdateAdmin.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventUpdateAdmin.getParticipantLimit()).ifPresent(event::setParticipantLimit);

        if (eventUpdateAdmin.getLocation() != null) {
            event.setLocation(locationRepository.save(locationMapper.locationDtoToLocation(eventUpdateAdmin.getLocation())));
            ;
        }

        if (eventUpdateAdmin.getCategory() != null) {
            Category category = categoryRepository.findById(eventUpdateAdmin.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория " + eventUpdateAdmin.getCategory() + " не найдена"));
            event.setCategory(category);
        }

        Optional.ofNullable(eventUpdateAdmin.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(eventUpdateAdmin.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventUpdateAdmin.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(eventUpdateAdmin.getTitle()).ifPresent(event::setTitle);
    }

    private void updateEventState(Event event, String stateAction) {
        if (stateAction != null) {
            switch (stateAction) {
                case "REJECT_EVENT":
                    event.setState(CANCELED);
                    break;
                case "PUBLISH_EVENT":
                    event.setPublishedOn(LocalDateTime.now());
                    event.setState(PUBLISHED);
                    break;
                default:
                    throw new ValidationException("Такого действия не существует - " + stateAction);
            }
        }
    }
}
