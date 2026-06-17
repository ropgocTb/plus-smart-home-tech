package ru.yandex.practicum.shopping.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.shopping.store.ShoppingStoreOperations;
import ru.yandex.practicum.commerce.dto.shopping.store.PageProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.QuantityState;
import ru.yandex.practicum.commerce.request.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.shopping.store.service.StoreService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class StoreController implements ShoppingStoreOperations {

    private final StoreService service;

    @Override
    public PageProductDto getProductsByCategory(ProductCategory category, int page, int size, String[] sort) {
        log.info("try get products by category");
        return service.getProductsByCategory(category, page, size, sort);
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        log.info("try get product");
        return service.getProduct(productId);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        log.info("try add product");
        return service.addProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        log.info("try update product");
        return service.updateProduct(productDto);
    }

    @Override
    public Boolean removeProduct(UUID productId) {
        log.info("try remove product");
        return service.removeProduct(productId);
    }

    @Override
    public Boolean setProductQuantity(UUID productId, QuantityState quantityState) {
        log.info("try set product quantity");
        return service.setProductQuantity(new SetProductQuantityStateRequest(productId, quantityState));
    }
}
