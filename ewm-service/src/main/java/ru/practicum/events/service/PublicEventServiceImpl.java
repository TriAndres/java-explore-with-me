package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.UserEventParams;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.requests.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.events.model.State.PUBLISHED;

@Service
public class PublicEventServiceImpl extends EventBase implements PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final StatsClient statClient;

    public PublicEventServiceImpl(EventRepository eventRepository,
                                  EventMapper eventMapper,
                                  RequestRepository requestRepository,
                                  StatsClient statsClient) {
        super(requestRepository, statsClient);
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.statClient = statsClient;
    }

    @Override
    public EventDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие с id \"" + eventId + "\" не найдено"));
        if (!event.getState().equals(PUBLISHED)) {
            throw new NotFoundException("Событие с id " + eventId + " не опубликовано");
        }
        sendEndpointHit(request);
        List<Event> events = List.of(event);
        return eventMapper.eventToEventDto(event,
                getConfirmedRequests(events).getOrDefault(eventId, 0L),
                getViewsForEvents(events).getOrDefault(eventId, 0L));
    }

    @Override

    public List<EventShortDto> getAllPublicEvents(UserEventParams userEventParams, PageRequest pageRequest) {
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state"))
                .value(List.of(PUBLISHED)));
        if (!userEventParams.getText().isBlank()) {
            String searchText = "%" + userEventParams.getText().toLowerCase() + "%";
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), searchText),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchText)));
        }
        if (!userEventParams.getCategories().isEmpty()) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id"))
                    .value(userEventParams.getCategories()));
        }
        if (userEventParams.getPaid() != null) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), userEventParams.getPaid()));
        }
        if (userEventParams.getRangeStart() != null) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                    .greaterThanOrEqualTo(root.get("eventDate"), userEventParams.getRangeStart()));
        }
        if (userEventParams.getRangeEnd() != null) {
            specifications.add((root, query, criteriaBuilder) -> criteriaBuilder
                    .lessThanOrEqualTo(root.get("eventDate"), userEventParams.getRangeEnd()));
        }
        sendEndpointHit(userEventParams.getRequest());
        specifications = specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Specification<Event> s = specifications
                .stream()
                .reduce(Specification::and).orElse(null);
        List<Event> events = eventRepository.findAll(s, pageRequest).toList();

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);
        if (userEventParams.getOnlyAvailable()) {
            events = events
                    .stream()
                    .filter(event -> event.getParticipantLimit() > confirmedRequests.getOrDefault(event.getId(), 0L))
                    .collect(Collectors.toList());
        }
        Map<Long, Long> viewStats = getViewsForEvents(events);
        if (userEventParams.getSort() == null)
            return eventMapper.eventListToEventShortDtoList(events, viewStats, confirmedRequests);
        switch (userEventParams.getSort()) {
            case VIEWS:
                events = events
                        .stream()
                        .sorted(Comparator.comparing(event -> viewStats.getOrDefault(event.getId(), 0L)))
                        .collect(Collectors.toList());
            case EVENT_DATE:
                events = events
                        .stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
            default:
                return eventMapper.eventListToEventShortDtoList(events, viewStats, confirmedRequests);
        }
    }

    private void sendEndpointHit(HttpServletRequest request) {
        String app = "ewm-main-service";
        statClient.addHit(EndpointHitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
