package ru.practicum.ewm.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("SELECT new ru.practicum.ewm.stats.StatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC ")
    List<StatsDto> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.StatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC ")
    List<StatsDto> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.stats.StatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "AND s.uri IN ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC ")
    List<StatsDto> findAllByTimestampBetweenAndUriInAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.StatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC ")
    List<StatsDto> findAllByTimestampBetweenAndUniqueIp(LocalDateTime start, LocalDateTime end);
}
