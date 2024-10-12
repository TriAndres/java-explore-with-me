package ru.practicum.explorewithme.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.request.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByEventInitiatorIdAndEventId(Long userId, Long eventId);
}
