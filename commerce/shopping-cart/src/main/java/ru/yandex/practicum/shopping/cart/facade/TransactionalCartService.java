package ru.yandex.practicum.shopping.cart.facade;

import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TransactionalCartService {

    ShoppingCartDto getCart(String username);

    ShoppingCartDto addProductsToCart(String username, Map<UUID, Integer> products);

    void deactivateCart(String username);

    ShoppingCartDto removeProductsFromCart(String username, List<UUID> productsIds);

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest quantityRequest);
}
