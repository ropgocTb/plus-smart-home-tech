package ru.yandex.practicum.commerce.request.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.dto.warehouse.DimensionDto;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class NewProductInWarehouseRequest {

    @NotNull
    private UUID productId;

    @NotNull
    @Valid
    private DimensionDto dimension;

    private Boolean fragile;

    @NotNull
    @Min(1)
    private Double weight;
}
