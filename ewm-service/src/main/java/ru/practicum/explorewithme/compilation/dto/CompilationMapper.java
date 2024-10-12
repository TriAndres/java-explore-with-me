package ru.practicum.explorewithme.compilation.dto;

import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.event.dto.EventMapper;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        Boolean pinned;
        if (newCompilationDto.getPinned() == null) {
            pinned = false;
        } else {
            pinned = newCompilationDto.getPinned();
        }
        return new Compilation(
                null,
                pinned,
                newCompilationDto.getTitle(),
                events
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> events;
        if (compilation.getEvents() != null) {
            events = EventMapper.listToEventShortDto(new ArrayList<>(compilation.getEvents()));
        } else {
            events = null;
        }

        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                events
        );
    }

    public static List<CompilationDto> toListCompilationDto(List<Compilation> compilations) {
        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            compilationDtos.add(toCompilationDto(compilation));
        }
        return compilationDtos;
    }
}