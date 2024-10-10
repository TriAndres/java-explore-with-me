package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Stats;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query("SELECT new ru.practicum.model.ViewStats(a.name, s.uri, COUNT(s.ip)) " +
            "FROM App a " +
            "JOIN Stats s ON s.app.id = a.id " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN :uris " +
            "GROUP BY a.name, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(a.name, s.uri, COUNT(s.ip)) " +
            "FROM App a " +
            "JOIN Stats s ON s.app.id = a.id " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY a.name, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findStatsWithOutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(a.name, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM App a " +
            "JOIN Stats s ON s.app.id = a.id " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN :uris " +
            "GROUP BY a.name, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findStatsUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(a.name, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM App a " +
            "JOIN Stats s ON s.app = a " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY a.name, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findStatsUniqueWithOutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}