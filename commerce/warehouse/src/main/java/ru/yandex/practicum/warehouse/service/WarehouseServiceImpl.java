package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[new SecureRandom().nextInt(ADDRESSES.length)];

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

    @Override
    public AddressDto getAddress() {
        log.info("Returning warehouse address");
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkQuantity(ShoppingCartDto cartDto) {
        log.info("Checking quantity for: {}", cartDto);

        verifyProductsPresence(cartDto);

        double deliveryWeight = 0.0;
        double deliveryVolume = 0.0;
        boolean fragile = false;

        for (WarehouseProduct product : repository.findAllById(cartDto.getProducts().keySet())) {
            int presentQuantity = product.getQuantity();
            int requestedQuantity = cartDto.getProducts().get(product.getProductId());

            if (presentQuantity < requestedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Warehouse has not enough quantity of product: " +
                        product.getProductId());
            } else {
                deliveryWeight += product.getWeight();
                deliveryVolume += product.getDepth() * product.getWidth() * product.getHeight();
                if (product.isFragile())
                    fragile = true;
            }
        }

        return new BookedProductsDto(deliveryWeight, deliveryVolume, fragile);
    }

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        log.info("Adding new product: {}", request);
        if (repository.existsById(request.getProductId()))
            throw new SpecifiedProductAlreadyInWarehouseException("Product with id: " +
                    request.getProductId() + "already exists");

        repository.save(mapper.toEntity(request));
        log.info("Added new product: {}", request.getProductId());
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        log.info("Adding quantity to a product: {}", request.getProductId());

        WarehouseProduct product = repository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Product with id: " +
                        request.getProductId() + " does not exist in the warehouse"));

        product.setQuantity(request.getQuantity() + product.getQuantity());

        repository.save(product);
        log.info("Added quantity to a product: {}, new quantity: {}", request.getProductId(), request.getQuantity());
    }

    private void verifyProductsPresence(ShoppingCartDto cartDto) {
        Set<UUID> productsInCart = cartDto.getProducts().keySet();

        Set<UUID> existingProducts = repository.findAllById(productsInCart).stream()
                .map(WarehouseProduct::getProductId)
                .collect(Collectors.toSet());

        if (productsInCart.size() != existingProducts.size()) {

            List<String> nonPresentProducts = existingProducts.stream()
                    .filter(id -> !existingProducts.contains(id))
                    .map(UUID::toString)
                    .toList();

            throw new NoSpecifiedProductInWarehouseException("Some products from cart are not present " +
                    "in the warehouse: " + nonPresentProducts);
        }
    }
}
