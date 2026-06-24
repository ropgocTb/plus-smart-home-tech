package ru.yandex.practicum.shopping.cart.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.exception.shopping.cart.CartIsNotActiveException;
import ru.yandex.practicum.commerce.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.shopping.cart.repository.ShoppingCartRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionalCartServiceImpl implements TransactionalCartService {

    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper mapper;

    @Override
    public ShoppingCartDto getCart(String username) {
        log.info("Getting cart: {}", username);
        Optional<ShoppingCart> cartOptional = repository.findByUsername(username);

        if (cartOptional.isEmpty()) {
            log.info("No cart for user: {}, creating a new cart", username);
            ShoppingCart cart = new ShoppingCart();
            cart.setUsername(username);
            cart.setActive(true);
            cart.setProducts(new HashMap<>());

            return mapper.mapToDto(repository.save(cart));
        } else {
            log.info("Got cart: {}", cartOptional.get());
            return mapper.mapToDto(cartOptional.get());
        }
    }


    @Override
    public ShoppingCartDto addProductsToCart(String username, Map<UUID, Integer> products) {
        log.info("Adding: {}, to cart: {}", products, username);

        getCart(username);

        ShoppingCart cart = repository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new CartIsNotActiveException("Cart is not active thus cannot receive products"));

        products.forEach((uid, quantity) -> {
            cart.getProducts().merge(uid, quantity, Integer::sum);
            log.info("added {}", cart.getProducts().keySet());
        });

        return mapper.mapToDto(cart);
    }

    @Override
    public void deactivateCart(String username) {
        log.info("Deactivating cart: {}", username);

        getCart(username);

        ShoppingCart cart = repository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new CartIsNotActiveException("Cart is not active thus cannot be deactivated"));

        cart.setActive(false);

        repository.save(cart);
        log.info("cart: {}, deactivated", username);
    }

    @Override
    public ShoppingCartDto removeProductsFromCart(String username, List<UUID> productsIds) {
        log.info("Removing products: {}, from cart: {}", productsIds, username);

        getCart(username);

        ShoppingCart cart = repository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new CartIsNotActiveException("Cart is not active thus cannot receive products"));

        productsIds.forEach(productId -> {
            if (!cart.getProducts().containsKey(productId)) {
                throw new NoProductsInShoppingCartException("No product " + productId + " in cart " + username);
            }
            cart.getProducts().remove(productId);
        });

        return mapper.mapToDto(repository.save(cart));
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest quantityRequest) {
        log.info("Changing quantity: {},  of cart: {}", quantityRequest, username);

        getCart(username);

        ShoppingCart cart = repository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new CartIsNotActiveException("Cart is not active thus " +
                        "cannot change quantity of products"));

        if (!cart.getProducts().containsKey(quantityRequest.getProductId()))
            throw new NoProductsInShoppingCartException("There is no product " + quantityRequest.getProductId() + "in cart");

        if (quantityRequest.getNewQuantity() <= 0)
            cart.getProducts().remove(quantityRequest.getProductId());
        else
            cart.getProducts().put(quantityRequest.getProductId(), quantityRequest.getNewQuantity());

        return mapper.mapToDto(repository.save(cart));
    }
}
