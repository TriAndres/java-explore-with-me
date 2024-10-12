package ru.practicum.explorewithme;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"app", "uri"})
public class ViewStatsDto implements Comparable<ViewStatsDto> {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @PositiveOrZero
    @NotNull
    private Integer hits;

    public ViewStatsDto(String app, String uri) {
        this.app = app;
        this.uri = uri;
        this.hits = 1;
    }

    @Override
    public int compareTo(ViewStatsDto o) {
        return o.getHits().compareTo(this.getHits());
    }
}