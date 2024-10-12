package ru.practicum.explorewithme.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.util.PageCreatorUtil;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.CompilationMapper;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CompilationServiceIml implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        PageRequest page = PageCreatorUtil.createPage(from, size);
        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, page);
        } else {
            compilations = compilationRepository.findAllBy(page);
        }
        return CompilationMapper.toListCompilationDto(compilations);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new CompilationNotFoundException("Подборки событий с id = " + compId + " не существует"));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> existedEvents;
        if (newCompilationDto.getEvents() != null) {
            existedEvents = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        } else {
            existedEvents = null;
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, existedEvents)));
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new CompilationNotFoundException("Подборки событий с id = " + compId + " не существует"));
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto patchCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new CompilationNotFoundException("Подборки событий с id = " + compId + " не существует"));
        Set<Event> existedEvents;
        if (updateCompilationRequest.getEvents() != null) {
            existedEvents = new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents()));
        } else {
            existedEvents = null;
        }

        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(existedEvents);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }
}