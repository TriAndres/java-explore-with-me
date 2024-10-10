package ru.practicum.mainservice.dto.location;

import lombok.*;

@Data
@Builder
public class LocationDto {
    private float lat;
    private float lon;
}