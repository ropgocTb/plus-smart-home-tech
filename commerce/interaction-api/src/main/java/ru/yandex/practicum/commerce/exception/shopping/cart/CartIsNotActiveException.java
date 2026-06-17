package ru.yandex.practicum.commerce.exception.shopping.cart;

public class CartIsNotActiveException extends RuntimeException {
    public CartIsNotActiveException(String message) {
        super(message);
    }
}
