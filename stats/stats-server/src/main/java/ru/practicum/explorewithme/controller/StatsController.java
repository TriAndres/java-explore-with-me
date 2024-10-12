package ru.practicum.explorewithme.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.model.HitResponse;
import ru.practicum.explorewithme.service.StatsService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitResponse postEndpointHit(@Valid @RequestBody EndpointHitDto hitDto) {
        log.info("Получен запрос POST /hit");
        return statsService.addEndpointHit(hitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") String start,
                                       @RequestParam(name = "end") String end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Получен запрос GET /stats");
        return statsService.getStats(start, end, uris, unique);
    }

    @GetMapping("/unique")
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkUnique(@RequestParam(name = "uri") String uri,
                               @RequestParam(name = "ip") String ip) {
        return statsService.checkUnique(uri, ip);
    }
}