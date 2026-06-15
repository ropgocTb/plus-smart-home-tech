package ru.yandex.practicum.commerce.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class BookedProductsDto {

    private double deliveryWeight;
    private double deliveryVolume;
    private Boolean fragile;
}
