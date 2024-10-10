package ru.practicum;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class StatClient {

    private final WebClient webClient;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatClient(@Value("${STATS_SERVER_URL}") String uri) {
        this.webClient = WebClient.create(uri);
    }

    public EndpointHitDto createHit(@Valid EndpointHitDto endpointHitDto) {
        return webClient.post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(endpointHitDto)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public List<ViewStatsDto> getStats(StatsDto statsDto) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", statsDto.getStart().format(formatter))
                        .queryParam("end", statsDto.getEnd().format(formatter))
                        .queryParam("uris", statsDto.getUris())
                        .queryParam("unique", statsDto.getUnique())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ViewStatsDto.class)
                .collectList()
                .block();
    }
}