package ru.practicum.compilations.service;

import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationUpdateDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.repository.RequestRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminCompilationServiceImpl extends CompilationBase implements AdminCompilationService {
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;

    private final CompilationRepository compilationRepository;

    private final EventMapper eventMapper;

    public AdminCompilationServiceImpl(EventRepository eventRepository,
                                       CompilationRepository compilationRepository,
                                       CompilationMapper compilationMapper,
                                       EventMapper eventMapper,
                                       RequestRepository requestRepository,
                                       StatsClient statsClient) {
        super(requestRepository, statsClient);
        this.eventRepository = eventRepository;
        this.compilationMapper = compilationMapper;
        this.compilationRepository = compilationRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        HashSet<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null) {
            events = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        }
        Compilation compilation = compilationMapper.compilationDtoToCompilation(newCompilationDto, events);

        List<EventShortDto> eventShortDtos = createEventShortDto(compilation);

        return compilationMapper.compilationToCompilationDto(compilationRepository.save(compilation), eventShortDtos);
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compilationRepository.findById(compilationId).orElseThrow(()
                -> new NotFoundException("Компиляция с id: " + compilationId + " Не найдена."));
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(()
                -> new NotFoundException("Компиляции не найдены"));
        List<Long> ids = compilationUpdateDto.getEvents();
        if (ids == null) {
            throw new NotFoundException("События не найдены");
        }
        HashSet<Event> events = new HashSet<>(eventRepository.findAllById(ids));
        if (compilationUpdateDto.getEvents() != null) {
            compilation.setEvents(events);
        }
        if (compilationUpdateDto.getPinned() != null) {
            compilation.setPinned(compilationUpdateDto.getPinned());
        }
        if (compilationUpdateDto.getTitle() != null) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }

        List<EventShortDto> eventShortDtos = createEventShortDto(compilation);

        return compilationMapper.compilationToCompilationDto(compilationRepository.save(compilation), eventShortDtos);
    }

    private List<EventShortDto> createEventShortDto(Compilation compilation) {
        Set<Event> allEvents = compilation.getEvents();
        Map<Long, Long> views = getViewsForEvents(allEvents);
        Map<Long, Long> confirmed = getConfirmedRequests(allEvents);

        return allEvents.stream()
                .map(event -> eventMapper.eventToEventShortDto(event,
                        views.getOrDefault(event.getId(), 0L),
                        confirmed.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());


    }
}
