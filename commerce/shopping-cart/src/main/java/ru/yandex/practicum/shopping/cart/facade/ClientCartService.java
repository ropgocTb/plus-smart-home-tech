package ru.yandex.practicum.shopping.cart.facade;

import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.Map;
import java.util.UUID;

public interface ClientCartService {

    ShoppingCartDto addProductsToCart(String username, Map<UUID, Integer> products);

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest quantityRequest);
}
