package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> findAllByTimestampIsAfterAndTimestampIsBefore(LocalDateTime start, LocalDateTime end);

    List<EndpointHit> findAllByTimestampIsAfterAndTimestampIsBeforeAndUriIsIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<EndpointHit> findAllByIpAndUri(String uri, String ip);
}