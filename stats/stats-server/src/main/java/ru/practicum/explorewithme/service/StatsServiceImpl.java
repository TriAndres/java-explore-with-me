package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.exceptions.ValidationException;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.EndpointHitMapper;
import ru.practicum.explorewithme.model.HitResponse;
import ru.practicum.explorewithme.repository.HitRepository;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String MESSAGE = "Информация сохранена";
    private final HitRepository hitRepository;

    @Override
    public HitResponse addEndpointHit(EndpointHitDto hitDto) {
        EndpointHit endpointHit = hitRepository.save(EndpointHitMapper.toEndpointHit(hitDto));
        log.info("EndpointHit: " + endpointHit + " успешно сохранен");
        return new HitResponse(MESSAGE);
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDF = LocalDateTime.parse(URLDecoder.decode(start), FORMATTER);
        LocalDateTime endDF = LocalDateTime.parse(URLDecoder.decode(end), FORMATTER);
        if (endDF.isBefore(startDF)) {
            throw new ValidationException("Время start и end указаны неверно");
        }

        List<EndpointHit> hits = new ArrayList<>();
        if (uris == null) {
            hits.addAll(hitRepository.findAllByTimestampIsAfterAndTimestampIsBefore(startDF, endDF));
        } else {
            hits.addAll(hitRepository.findAllByTimestampIsAfterAndTimestampIsBeforeAndUriIsIn(startDF, endDF, uris));
        }
        List<EndpointHitDto> hitsDTO = EndpointHitMapper.listToEndpointHitDto(hits);

        if (unique) {
            Set<EndpointHitDto> uniqueHitsDto = new HashSet<>(hitsDTO);
            hitsDTO = new ArrayList<>(uniqueHitsDto);
        }

        List<ViewStatsDto> stats = new ArrayList<>();

        for (EndpointHitDto hitDto : hitsDTO) {
            ViewStatsDto viewStatsDto = new ViewStatsDto(hitDto.getApp(), hitDto.getUri());

            if (stats.contains(viewStatsDto)) {
                for (ViewStatsDto existedViewStatsDto : stats) {
                    if (existedViewStatsDto.equals(viewStatsDto)) {
                        existedViewStatsDto.setHits(existedViewStatsDto.getHits() + 1);
                    }
                }
            } else {
                stats.add(viewStatsDto);
            }
        }

        List<ViewStatsDto> sortedStats = stats.stream().sorted(ViewStatsDto::compareTo).collect(Collectors.toList());

        log.info("Получена статистика: " + stats);
        return sortedStats;
    }

    @Override
    public Boolean checkUnique(String uri, String ip) {
        List<EndpointHit> hits = hitRepository.findAllByIpAndUri(ip, uri);
        return hits.isEmpty();
    }
}