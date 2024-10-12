package ru.practicum.explorewithme.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "requests")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created;

    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @JoinColumn(name = "requester_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;

    @Enumerated(EnumType.ORDINAL)
    private State status;
}