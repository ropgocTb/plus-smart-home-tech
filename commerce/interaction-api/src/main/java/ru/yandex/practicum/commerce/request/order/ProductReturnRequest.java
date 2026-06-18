package ru.yandex.practicum.commerce.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ProductReturnRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private Map<UUID, Integer> products;
}
