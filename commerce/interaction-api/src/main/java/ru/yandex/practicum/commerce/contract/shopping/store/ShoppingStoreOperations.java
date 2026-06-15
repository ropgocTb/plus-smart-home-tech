package ru.yandex.practicum.commerce.contract.shopping.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.shopping.store.PageProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.QuantityState;

import java.util.UUID;

public interface ShoppingStoreOperations {

    @GetMapping
    PageProductDto getProductsByCategory(@RequestParam ProductCategory category,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                         @RequestParam(defaultValue = "20") @Positive int size,
                                         @RequestParam(required = false) String[] sort);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);

    @PutMapping
    ProductDto addProduct(@RequestBody @Valid @NotNull ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@Valid @NotNull @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProduct(@RequestBody @NotNull UUID productId);

    @PostMapping("/quantityState")
    Boolean setProductQuantity(@RequestParam UUID productId,
                               @RequestParam QuantityState quantityState);
}
