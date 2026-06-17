package ru.yandex.practicum.shopping.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.exception.ErrorResponse;
import ru.yandex.practicum.commerce.exception.shopping.cart.CartIsNotActiveException;
import ru.yandex.practicum.commerce.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.exception.shopping.cart.NotAuthorizedUserException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(CartIsNotActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFound(CartIsNotActiveException ex) {
        return new ErrorResponse("CartIsNotActive exception: " + ex.getMessage());
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFound(NoProductsInShoppingCartException ex) {
        return new ErrorResponse("NoProductsInShoppingCart exception: " + ex.getMessage());
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleNotFound(NotAuthorizedUserException ex) {
        return new ErrorResponse("NotAuthorizedUser exception: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse("Internal server error: " + e.getMessage());
    }
}
