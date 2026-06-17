package ru.yandex.practicum.shopping.cart.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseClient;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientCartServiceImpl implements ClientCartService {

    private final WarehouseClient warehouseClient;
    private final TransactionalCartService transactionalCartService;

    @Override
    public ShoppingCartDto addProductsToCart(String username, Map<UUID, Integer> products) {
        ShoppingCartDto cartDto = transactionalCartService.getCart(username);

        products.forEach((uid, quantity) -> {
            cartDto.getProducts().merge(uid, quantity, Integer::sum);

            //неизвестно как должен происходить расчет объема и веса заказа
            //поэтому пусть проверяется на доступность вся корзина целиком
            log.info("added {}", cartDto.getProducts().keySet());
        });

        warehouseClient.checkQuantity(cartDto);
        return transactionalCartService.addProductsToCart(username, products);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest quantityRequest) {
        ShoppingCartDto cartDto = transactionalCartService.getCart(username);

        if (!cartDto.getProducts().containsKey(quantityRequest.getProductId()))
            throw new NoProductsInShoppingCartException("There is no product " +
                    quantityRequest.getProductId() + "in cart");

        if (quantityRequest.getNewQuantity() <= 0)
            cartDto.getProducts().remove(quantityRequest.getProductId());
        else
            cartDto.getProducts().put(quantityRequest.getProductId(), quantityRequest.getNewQuantity());

        warehouseClient.checkQuantity(cartDto);
        return transactionalCartService.changeQuantity(username, quantityRequest);
    }
}
