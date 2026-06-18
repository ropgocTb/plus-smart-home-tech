package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    AddressDto getAddress();

    BookedProductsDto checkQuantity(ShoppingCartDto cartDto);

    void addNewProduct(NewProductInWarehouseRequest request);

    void addProduct(AddProductToWarehouseRequest request);

    void shipProducts(ShippedToDeliveryRequest request);

    void returnProducts(Map<UUID, Integer> products);

    BookedProductsDto assembleProducts(AssemblyProductsForOrderRequest request);
}
