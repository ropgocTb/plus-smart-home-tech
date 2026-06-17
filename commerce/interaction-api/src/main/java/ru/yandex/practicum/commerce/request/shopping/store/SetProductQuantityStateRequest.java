package ru.yandex.practicum.commerce.request.shopping.store;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.dto.shopping.store.QuantityState;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SetProductQuantityStateRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantityState;
}
