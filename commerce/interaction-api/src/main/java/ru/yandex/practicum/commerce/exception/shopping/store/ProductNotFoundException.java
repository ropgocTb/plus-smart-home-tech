package ru.yandex.practicum.commerce.exception.shopping.store;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
