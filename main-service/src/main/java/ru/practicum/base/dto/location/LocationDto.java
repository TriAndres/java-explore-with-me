package ru.practicum.base.dto.location;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    @NonNull
    private Float lat;
    @NonNull
    private Float lon;
}