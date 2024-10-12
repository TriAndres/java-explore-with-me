package ru.practicum.explorewithme.compilation.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.explorewithme.event.model.Event;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "compilations")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean pinned;

    @Column
    private String title;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "event_compilations", joinColumns = @JoinColumn(name = "compilation_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events = new HashSet<>();
}
