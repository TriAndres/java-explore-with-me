package ru.practicum.explorewithme;

import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("http://stats-server:9090") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> addHit(EndpointHitDto hitDto) {
        return post("/hit", hitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, @Nullable String[] uris, Boolean unique) {
        Map<String, Object> parameters;

        if (uris == null) {
            parameters = Map.of(
                    "start", URLEncoder.encode(start, StandardCharsets.UTF_8),
                    "end", URLEncoder.encode(end, StandardCharsets.UTF_8),
                    "unique", unique);
            return get("/stats?start={start}&end={end}&unique={unique}", parameters);
        } else {
            parameters = Map.of(
                    "start", URLEncoder.encode(start, StandardCharsets.UTF_8),
                    "end", URLEncoder.encode(end, StandardCharsets.UTF_8),
                    "uris", uris,
                    "unique", unique);
            return get("/stats?start={start}&end={end}&unique={unique}&uris={uris}", parameters);
        }
    }

    public Boolean checkUnique(String uri, String ip) {
        Map<String, Object> parameters = Map.of("ip", ip,
                "uri", uri);

        Object response = get("/unique?uri={uri}&ip={ip}", parameters).getBody();
        String re = response.toString();

        return response.equals(true);
    }
}
