package ru.practicum.events.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateUser;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final UserMapper userMapper;

    private final CategoryMapper categoryMapper;
    private final LocationMapper locationMapper;

    public EventDto eventToEventDto(Event event, Long confirmedRequests, Long views) {
        String publishedOn = null;
        if (event.getPublishedOn() != null) {
            publishedOn = event.getPublishedOn().format(formatter);
        }
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                categoryMapper.categoryToCategoryDto(event.getCategory()),
                event.getPaid(),
                event.getEventDate().format(formatter),
                userMapper.userToUserShortDto(event.getInitiator()),
                event.getDescription(),
                event.getParticipantLimit(),
                event.getState(),
                event.getCreatedOn().format(formatter),
                locationMapper.locationToLocationDto(event.getLocation()),
                event.getRequestModeration(),
                confirmedRequests,
                publishedOn,
                views
        );
    }

    public Event newEventDtoToEvent(NewEventDto dto, Category category, User user, Location location) {

        return new Event(
                0L,
                user,
                dto.getAnnotation(),
                category,
                LocalDateTime.now(),
                dto.getDescription(),
                dto.getEventDate(),
                location,
                dto.getPaid(),
                dto.getParticipantLimit(),
                null,
                dto.getRequestModeration(),
                null,
                dto.getTitle());
    }

    public Event eventToEventUpdateUser(Event event, EventUpdateUser eventUpdateDto) {
        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }
        if (eventUpdateDto.getLocation() != null) {
            locationMapper.locationDtoToLocation(eventUpdateDto.getLocation());
        }
        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }
        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }
        if (eventUpdateDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateDto.getRequestModeration());
        }
        if (eventUpdateDto.getTitle() != null) {
            event.setTitle(eventUpdateDto.getTitle());
        }
        return event;
    }

    public List<EventShortDto> eventListToEventShortDtoList(List<Event> events,
                                                            Map<Long, Long> viewStatMap,
                                                            Map<Long, Long> confirmedRequests) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            Long views = viewStatMap.getOrDefault(event.getId(), 0L);
            Long confirmedRequestsCount = confirmedRequests.getOrDefault(event.getId(), 0L);
            dtos.add(new EventShortDto(
                    event.getAnnotation(),
                    categoryMapper.categoryToCategoryDto(event.getCategory()),
                    confirmedRequestsCount,
                    event.getEventDate().format(formatter),
                    event.getId(),
                    userMapper.userToUserShortDto(event.getInitiator()),
                    event.getPaid(),
                    event.getTitle(),
                    views
            ));
        }
        dtos.sort(Comparator.comparing(EventShortDto::getViews).reversed());

        return dtos;
    }

    public EventShortDto eventToEventShortDto(Event event, Long views, Long confirmed) {
        return new EventShortDto(
                event.getAnnotation(),
                categoryMapper.categoryToCategoryDto(event.getCategory()),
                confirmed,
                event.getEventDate().format(formatter),
                event.getId(),
                userMapper.userToUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                views);
    }
}
