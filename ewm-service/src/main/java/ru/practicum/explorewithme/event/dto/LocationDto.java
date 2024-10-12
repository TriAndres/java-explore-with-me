package ru.practicum.explorewithme.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationDto {

    private Float lat;
    private Float lon;
}