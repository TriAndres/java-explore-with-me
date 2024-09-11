package ru.practicum.ewm;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.stats.StatsRequestDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                           @Nullable List<String> uris, boolean unique) {
        Map<String, Object> parameters = new HashMap<>(Map.of(
                "start", encodeLocalDateTime(start),
                "end", encodeLocalDateTime(end),
                "unique", unique));
        if (uris != null) {
            parameters.put("uris", uris);
        }
        return get(StatsConstants.API_STATS_PREFIX + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> addStat(StatsRequestDto statsRequestDto) {
        return post(StatsConstants.API_HIT_PREFIX, statsRequestDto);
    }

    private String encodeLocalDateTime(LocalDateTime localDateTime) {
        return URLEncoder.encode(
                localDateTime.format(StatsConstants.DATE_FORMATTER),
                StandardCharsets.UTF_8);
    }
}
