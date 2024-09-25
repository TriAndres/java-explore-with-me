package ru.practicum.ewm.base.dao;

import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.EventSearchCriteria;

@Repository
public interface EventCriteriaRepository {

    Page<Event> findAllWithFilters(Pageable pageable, EventSearchCriteria eventSearchCriteria);
}
