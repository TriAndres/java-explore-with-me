package ru.practicum.base.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;
import ru.practicum.base.dto.location.LocationDto;
import ru.practicum.base.util.notBlankNull.NotBlankNull;

import java.time.LocalDateTime;

public class UpdateEventRequest {
    @Length(min = 20, max = 2000)
    @NotBlankNull
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    @NotBlankNull
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto locationDto;
    private Boolean paid;
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;
    @Length(min = 3, max = 120)
    
    @NotBlankNull
    private  String title;
}
