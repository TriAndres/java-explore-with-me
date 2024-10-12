package ru.practicum.explorewithme.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.explorewithme.common.exceptions.PaginationException;

public class PageCreatorUtil {
    public static PageRequest createPage(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new PaginationException("Параметры пагинации заданы неверно");
        }
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    public static PageRequest createPage(int from, int size, String sort) {
        if (from < 0 || size <= 0) {
            throw new PaginationException("Параметры пагинации заданы неверно");
        }
        return PageRequest.of(from > 0 ? from / size : 0, size, Sort.by(sort));
    }
}