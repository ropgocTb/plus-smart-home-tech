package ru.yandex.practicum.commerce.dto.shopping.cart;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class ShoppingCartDto {

    @NotNull
    private UUID cartId;

    @NotNull
    private Map<UUID, Integer> products;
}
