package ru.practicum.explorewithme.event.dto;

import ru.practicum.explorewithme.event.model.Location;

public class LocationMapper {

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }

    public static Location toLocation(LocationDto locationDto) {
        return new Location(
                null,
                locationDto.getLat(),
                locationDto.getLon()
        );
    }
}