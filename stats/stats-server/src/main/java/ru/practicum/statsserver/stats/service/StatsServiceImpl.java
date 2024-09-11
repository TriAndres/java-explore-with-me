package ru.practicum.statsserver.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.stats.mapper.EndpointHitMapper;
import ru.practicum.statsserver.stats.model.EndpointHit;
import ru.practicum.statsserver.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsRepository statsRepository;
    private final EndpointHitMapper endpointHitMapper;

    @Override
    @Transactional
    public void create(EndpointHitDto endpointHitDto) {
        log.info("StatsService: Beginning of method execution create().");

        log.info("StatsService.create(): Mapping from dto.");
        EndpointHit endpointHit = endpointHitMapper.toEndpointHit(endpointHitDto);

        log.info("StatsService.create(): Add endpoint hit to database.");
        statsRepository.save(endpointHit);
        log.info("StatsService.create(): EndpointHit saved successfully.");
    }

    @Override
    public List<ViewStatsDto> getStats(String start,
                                       String end,
                                       List<String> uris,
                                       boolean unique) {
        log.info("StatsService: Beginning of method execution getStats().");
        log.info("StatsService.getStats(): Parsing date time.");
        LocalDateTime startTime = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(end, DATE_TIME_FORMATTER);

        List<EndpointHit> hits = new ArrayList<>();

        log.info("StatsService.getStats(): Checking for the existence of a uris list.");
        if (uris != null && !uris.isEmpty()) {
            log.info("StatsService.getStats(): Getting hits with uris list.");
            hits.addAll(statsRepository.findByTimestampBetweenAndUriIn(startTime, endTime, uris));
        } else {
            log.info("StatsService.getStats(): Getting hits without uris list.");
            hits.addAll(statsRepository.findByTimestampBetween(startTime, endTime));
        }

        log.info("StatsService.getStats(): Checking for the existence of a unique parameter.");
        if (unique) {
            log.info("StatsService.getStats(): Getting hits with unique parameter.");
            Map<String, EndpointHit> uniqueHitsByIp = new HashMap<>();

            for (EndpointHit hit : hits) {
                String ip = hit.getIp();
                uniqueHitsByIp.putIfAbsent(ip, hit);
            }
            log.info("StatsService.getStats(): A list with unique hits was received successfully.");
            log.info("StatsService.getStats(): Collecting statistics based on a list uniqueHitsByIp.");
            return toViewStatsDtoList(uniqueHitsByIp.values());
        }

        log.info("StatsService.getStats(): Collecting statistics based on a list hits.");
        return toViewStatsDtoList(hits);
    }

    private List<ViewStatsDto> toViewStatsDtoList(Collection<EndpointHit> hits) {
        log.info("StatsService: Beginning of method execution toViewStatsDtoList().");
        log.info("StatsService.toViewStatsDtoList(): Start collecting statistics.");
        Map<String, Map<String, Long>> groupedStats = hits.stream()
                .collect(Collectors.groupingBy(EndpointHit::getApp,
                        Collectors.groupingBy(EndpointHit::getUri, Collectors.counting())));

        List<ViewStatsDto> result = groupedStats.entrySet().stream()
                .flatMap(appEntry -> appEntry.getValue().entrySet().stream()
                        .map(uriEntry -> ViewStatsDto.builder()
                                .app(appEntry.getKey())
                                .uri(uriEntry.getKey())
                                .hits(uriEntry.getValue().intValue())
                                .build()))
                .sorted(Comparator.comparing(ViewStatsDto::getHits).reversed())
                .toList();
        log.info("StatsService.toViewStatsDtoList(): Statistics successfully collected.");
        return result;
    }
}
