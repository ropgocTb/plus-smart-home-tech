package ru.yandex.practicum.commerce.contract.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseOperations {

    @GetMapping("/address")
    AddressDto getAddress();

    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody @NotNull @Valid ShoppingCartDto cart);

    @PostMapping("/add")
    void addProduct(@RequestBody @NotNull @Valid AddProductToWarehouseRequest request);

    @PutMapping
    void addNewProduct(@RequestBody @NotNull @Valid NewProductInWarehouseRequest request);

    @PostMapping("/shipped")
    void shipProducts(@RequestBody @Valid ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnProducts(@RequestBody Map<UUID, Integer> products);

    @PostMapping("/assembly")
    BookedProductsDto assembleProducts(@RequestBody @Valid AssemblyProductsForOrderRequest request);
}
