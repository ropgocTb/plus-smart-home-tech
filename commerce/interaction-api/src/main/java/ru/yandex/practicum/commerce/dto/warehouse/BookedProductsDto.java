package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class BookedProductsDto {

    @NotNull
    private double deliveryWeight;

    @NotNull
    private double deliveryVolume;

    @NotNull
    private Boolean fragile;
}
