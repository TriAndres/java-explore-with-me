package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.CountDto;
import ru.practicum.events.model.Event;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.requests.model.Status;
import ru.practicum.requests.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class EventBase {

    private final RequestRepository requestRepository;

    private final StatsClient statsClient;

    protected Map<Long, Long> getViewsForEvents(List<Event> events) {
        Map<String, Long> eventUrisAndIds = events.stream()
                .collect(Collectors.toMap(
                        event -> String.format("/events/%s", event.getId()),
                        Event::getId
                ));
        List<LocalDateTime> startDates = events.stream()
                .map(Event::getCreatedOn)
                .collect(Collectors.toList());
        LocalDateTime startDate = startDates.stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);
        Map<Long, Long> statsMap = new HashMap<>();

        if (startDate != null) {
            List<ViewStatsDto> stats = statsClient.getStats(
                    LocalDateTime.of(2020, 1, 1, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.of(2025, 12, 31, 23, 59, 59)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), List.copyOf(eventUrisAndIds.keySet()), true);
            statsMap = stats.stream().collect(Collectors.toMap(
                    statsDto -> parseEventIdFromUrl(statsDto.getUri()),
                    ViewStatsDto::getHits
            ));
        }
        return statsMap;
    }

    private Long parseEventIdFromUrl(String url) {
        String[] parts = url.split("/events/");
        if (parts.length == 2) {
            return Long.parseLong(parts[1]);
        }
        return -1L;
    }

    protected Map<Long, Long> getConfirmedRequests(List<Event> events) {
        if (events.isEmpty()) return Collections.emptyMap();
        List<Long> ids = events.stream().map(Event::getId).collect(Collectors.toList());
        List<CountDto> results = requestRepository.findByStatus(ids, Status.CONFIRMED);
        return results.stream().collect(Collectors.toMap(CountDto::getEventId, CountDto::getCount));
    }

}
