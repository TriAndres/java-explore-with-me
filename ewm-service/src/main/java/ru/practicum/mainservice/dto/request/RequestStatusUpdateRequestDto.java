package ru.practicum.mainservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
public class RequestStatusUpdateRequestDto {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private StatusOfUpdateRequest status;
}