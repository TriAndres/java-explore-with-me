package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.events.dto.CountDto;
import ru.practicum.events.model.Event;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.Status;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT new ru.practicum.events.dto.CountDto(r.event.id, COUNT(r.id)) " +
            "FROM Request AS r " +
            "WHERE r.status = :status AND r.event.id IN :ids " +
            "GROUP BY r.event.id")
    List<CountDto> findByStatus(@Param("ids") List<Long> ids,
                                @Param("status") Status status);

    List<Request> findAllByRequesterIdAndEventId(Long requesterId, Long eventId);

    Long countByEventIdAndStatus(Long eventId, Status status);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEvent(Event event);
}