package ru.practicum.statsserver.stats.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}


