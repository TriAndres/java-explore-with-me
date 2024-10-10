package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.server.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsServerRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.server.model.StatHits(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end AND (e.uri IN :uris OR :uris IS NULL) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<StatHits> findUniqueStatsWithUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.server.model.StatHits(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit as e " +
            "WHERE e.timestamp BETWEEN :start AND :end AND (e.uri IN :uris OR :uris IS NULL) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<StatHits> findAllStatsWithUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}