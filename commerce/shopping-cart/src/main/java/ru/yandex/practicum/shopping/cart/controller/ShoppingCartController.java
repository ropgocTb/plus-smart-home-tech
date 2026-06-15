package ru.yandex.practicum.shopping.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.shopping.cart.ShoppingCartOperations;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.shopping.cart.facade.ClientCartService;
import ru.yandex.practicum.shopping.cart.facade.TransactionalCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartController implements ShoppingCartOperations {

    private final TransactionalCartService service;
    private final ClientCartService clientService;

    @Override
    public ShoppingCartDto getCart(String username) {
        log.info("Try get cart");
        return service.getCart(username);
    }

    @Override
    public ShoppingCartDto addProductsToCart(String username, Map<UUID, Integer> products) {
        log.info("Try add products to cart");
        return clientService.addProductsToCart(username, products);
    }

    @Override
    public void deactivateCart(String username) {
        log.info("Try deactivate cart");
        service.deactivateCart(username);
    }

    @Override
    public ShoppingCartDto removeProductsFromCart(String username, List<UUID> products) {
        log.info("Try remove products from cart");
        return service.removeProductsFromCart(username, products);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        log.info("Try change quantity in cart");
        return clientService.changeQuantity(username, request);
    }
}
