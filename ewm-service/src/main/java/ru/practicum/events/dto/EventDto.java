package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.events.model.State;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private Long id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private Boolean paid;

    private String eventDate;

    private UserShortDto initiator;

    private String description;

    private Long participantLimit;

    private State state;

    private String createdOn;

    private LocationDto location;

    private Boolean requestModeration;

    private Long confirmedRequests;

    private String publishedOn;

    private Long views;
}
