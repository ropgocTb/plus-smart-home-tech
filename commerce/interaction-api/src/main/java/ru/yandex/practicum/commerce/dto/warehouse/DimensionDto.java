package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DimensionDto {

    @NotNull
    @PositiveOrZero
    private Double width;

    @NotNull
    @PositiveOrZero
    private Double height;

    @NotNull
    @PositiveOrZero
    private Double depth;
}
