package ru.practicum.location.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotNull
    private float lat;

    @NotNull
    private float lon;
}

