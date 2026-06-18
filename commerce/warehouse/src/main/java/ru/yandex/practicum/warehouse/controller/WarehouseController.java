package ru.yandex.practicum.warehouse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Slf4j
public class WarehouseController implements WarehouseOperations {

    private final WarehouseService service;

    @Override
    public AddressDto getAddress() {
        log.info("Trying to get warehouse address");
        return service.getAddress();
    }

    @Override
    public BookedProductsDto checkQuantity(ShoppingCartDto cartDto) {
        log.info("Trying to check quantity for cart: {}", cartDto);
        return service.checkQuantity(cartDto);
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        log.info("Trying add quantity to a product: {}", request);
        service.addProduct(request);
    }

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        log.info("Trying to add new product: {}", request);
        service.addNewProduct(request);
    }

    @Override
    public void shipProducts(ShippedToDeliveryRequest request) {
        log.info("Trying to ship product on request: {}", request);
        service.shipProducts(request);
    }

    @Override
    public void returnProducts(Map<UUID, Integer> products) {
        log.info("Trying to return products: {}", products.keySet());
        service.returnProducts(products);
    }

    @Override
    public BookedProductsDto assembleProducts(AssemblyProductsForOrderRequest request) {
        log.info("Trying to assemble products on request: {}", request);
        return service.assembleProducts(request);
    }
}
