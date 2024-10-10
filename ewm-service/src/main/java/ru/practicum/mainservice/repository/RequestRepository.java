package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.dto.request.RequestCountDto;
import ru.practicum.mainservice.model.Request;
import ru.practicum.mainservice.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventId(long eventId);

    List<Request> findAllByRequesterId(long userId);

    List<Request> findAllByIdIn(List<Long> ids);

    List<Request> findAllByEventIdInAndStatusEquals(List<Long> ids, RequestStatus status);

    int countAllByEventIdAndStatusEquals(long eventId, RequestStatus status);

    @Query("SELECT new ru.practicum.mainservice.dto.request.RequestCountDto(r.event.id, COUNT(r)) FROM Request r WHERE r.event.id IN :ids AND r.status = :status GROUP BY r.event.id")
    List<RequestCountDto> findAllConfirmedRequestsByEventIds(List<Long> ids, RequestStatus status);
}