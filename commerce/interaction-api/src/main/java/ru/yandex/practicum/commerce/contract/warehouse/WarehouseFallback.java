package ru.yandex.practicum.commerce.contract.warehouse;

import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;

public class WarehouseFallback implements WarehouseClient {

    @Override
    public AddressDto getAddress() {
        throw new RuntimeException("Warehouse is unavailable");
    }

    @Override
    public BookedProductsDto checkQuantity(ShoppingCartDto cart) {
        throw new RuntimeException("Warehouse is unavailable");
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        throw new RuntimeException("Warehouse is unavailable");
    }

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        throw new RuntimeException("Warehouse is unavailable");
    }
}
