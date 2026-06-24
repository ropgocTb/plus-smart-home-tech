package ru.yandex.practicum.commerce.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class OrderDto {

    @NotNull
    private UUID orderId;

    private OrderState state;

    @NotNull
    private Map<UUID, Integer> products;

    private UUID shoppingCartId;
    private UUID deliveryId;
    private UUID paymentId;
    private Double deliveryVolume;
    private Double deliveryWeight;
    private Boolean fragile;
    private Double totalPrice;
    private Double productPrice;
    private Double deliveryPrice;
}
