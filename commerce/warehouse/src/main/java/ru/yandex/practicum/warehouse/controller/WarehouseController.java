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
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.service.WarehouseService;

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
}
