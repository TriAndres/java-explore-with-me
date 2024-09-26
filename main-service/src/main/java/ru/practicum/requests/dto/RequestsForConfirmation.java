package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestsForConfirmation {
    private List<Long> requestIds;
    private String status;
}
