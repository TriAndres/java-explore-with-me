package ru.practicum.compilations.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.repository.RequestRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PublicCompilationServiceImpl extends CompilationBase implements PublicCompilationService {

    private final CompilationMapper compilationMapper;

    private final CompilationRepository compilationRepository;

    private final EventMapper eventMapper;

    public PublicCompilationServiceImpl(CompilationRepository compilationRepository,
                                        CompilationMapper compilationMapper,
                                        EventMapper eventMapper,
                                        RequestRepository requestRepository,
                                        StatsClient statsClient) {
        super(requestRepository, statsClient);
        this.compilationMapper = compilationMapper;
        this.compilationRepository = compilationRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pined, PageRequest pageRequest) {
        List<Compilation> compilations;
        if (pined == null) {
            compilations = compilationRepository.findAll(pageRequest).toList();
        } else {
            compilations = compilationRepository.findAllByPinned(pined, pageRequest);
        }
        Set<Event> allEvents = compilations.stream()
                .flatMap(compilation -> compilation.getEvents().stream())
                .collect(Collectors.toSet());
        Map<Long, Long> views = getViewsForEvents(allEvents);
        Map<Long, Long> confirmed = getConfirmedRequests(allEvents);

        List<CompilationDto> compilationDtos;

        compilationDtos = compilationMapper.compilationsListToCompilationDtoList(compilations, confirmed, views);
        return compilationDtos;
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(()
                -> new NotFoundException("Подборка с id: " + compilationId + " не найдена или недоступна"));

        Set<Event> allEvents = compilation.getEvents();
        Map<Long, Long> views = getViewsForEvents(allEvents);
        Map<Long, Long> confirmed = getConfirmedRequests(allEvents);

        List<EventShortDto> eventShortDtos = allEvents.stream()
                .map(event -> eventMapper.eventToEventShortDto(event,
                        views.getOrDefault(event.getId(), 0L),
                        confirmed.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());

        return compilationMapper.compilationToCompilationDto(compilation, eventShortDtos);
    }
}
