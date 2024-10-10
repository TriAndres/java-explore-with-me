package ru.practicum.mainservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestStatusUpdateResultDto {
    @NotNull
    private List<RequestDto> confirmedRequests;
    @NotNull
    private List<RequestDto> rejectedRequests;
}