package ru.yandex.practicum.commerce.contract.shopping.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartOperations {

    @GetMapping
    ShoppingCartDto getCart(@RequestParam @NotBlank String username);

    @PutMapping
    ShoppingCartDto addProductsToCart(@RequestParam @NotBlank String username,
                                      @RequestBody @NotNull Map<UUID, Integer> products);

    @DeleteMapping
    void deactivateCart(@RequestParam @NotBlank String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProductsFromCart(@RequestParam @NotBlank String username,
                                           @RequestBody @NotNull List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam @NotBlank String username,
                                   @RequestBody @NotNull @Valid ChangeProductQuantityRequest request);

}
