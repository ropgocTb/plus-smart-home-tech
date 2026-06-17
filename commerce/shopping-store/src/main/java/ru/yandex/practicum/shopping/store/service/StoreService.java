package ru.yandex.practicum.shopping.store.service;

import ru.yandex.practicum.commerce.dto.shopping.store.PageProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.request.shopping.store.SetProductQuantityStateRequest;

import java.util.UUID;

public interface StoreService {

    PageProductDto getProductsByCategory(ProductCategory category, int page, int size, String[] sort);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean removeProduct(UUID productId);

    Boolean setProductQuantity(SetProductQuantityStateRequest stateRequest);

    ProductDto getProduct(UUID productId);
}
