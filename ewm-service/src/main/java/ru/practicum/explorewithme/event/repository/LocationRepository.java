package ru.practicum.explorewithme.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findByLatAndLon(Float lat, Float lon);
}
