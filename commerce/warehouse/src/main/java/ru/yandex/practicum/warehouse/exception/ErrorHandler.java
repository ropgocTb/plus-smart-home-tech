package ru.yandex.practicum.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.exception.ErrorResponse;
import ru.yandex.practicum.commerce.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFound(NoSpecifiedProductInWarehouseException ex) {
        return new ErrorResponse("NoSpecifiedProductInWarehouseException exception: " + ex.getMessage());
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFound(ProductInShoppingCartLowQuantityInWarehouse ex) {
        return new ErrorResponse("ProductInShoppingCartLowQuantityInWarehouse exception: " + ex.getMessage());
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFound(SpecifiedProductAlreadyInWarehouseException ex) {
        return new ErrorResponse("SpecifiedProductAlreadyInWarehouse exception: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse("Internal server error: " + e.getMessage());
    }
}
