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
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.model.OrderBooking;
import ru.yandex.practicum.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[new SecureRandom().nextInt(ADDRESSES.length)];

    private final WarehouseRepository repository;
    private final OrderBookingRepository orderBookingRepository;
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

        List<UUID> insufficientProducts = new ArrayList<>();
        double deliveryWeight = 0.0;
        double deliveryVolume = 0.0;
        boolean fragile = false;

        for (WarehouseProduct product : repository.findAllById(cartDto.getProducts().keySet())) {
            int presentQuantity = product.getQuantity();
            int requestedQuantity = cartDto.getProducts().get(product.getProductId());

            if (presentQuantity < requestedQuantity) {
                insufficientProducts.add(product.getProductId());
            } else {
                deliveryWeight += product.getWeight() * requestedQuantity;
                deliveryVolume += product.getDepth() * product.getWidth() * product.getHeight() * requestedQuantity;
                if (product.isFragile())
                    fragile = true;
            }
        }

        if (!insufficientProducts.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Warehouse has not enough quantity of products: " +
                    insufficientProducts);
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

    @Override
    public void shipProducts(ShippedToDeliveryRequest request) {
        log.info("Shipping products for order: {}", request.getOrderId());
        OrderBooking booking = orderBookingRepository.findByOrderId(request.getOrderId());

        if (booking == null)
            throw new IllegalArgumentException("not found booking for request " + request.getOrderId());

        booking.setDeliveryId(request.getDeliveryId());
        OrderBooking saved = orderBookingRepository.save(booking);
        log.info("Products shipped, booking id: {}", saved.getBookingId());
    }

    @Override
    public void returnProducts(Map<UUID, Integer> products) {
        log.info("Returning products: {}", products.keySet());

        Set<UUID> productIds = products.keySet();
        List<WarehouseProduct> productList = repository.findAllById(productIds);

        Map<UUID, WarehouseProduct> stockMap = productList.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        for (Map.Entry<UUID, Integer> es : products.entrySet()) {
            UUID productId = es.getKey();
            Integer quantityToAdd = es.getValue();

            if (quantityToAdd == null || quantityToAdd <= 0) {
                continue;
            }

            WarehouseProduct product = stockMap.get(productId);
            if (product == null) {
                throw new NoSpecifiedProductInWarehouseException("Id was not found in the warehouse: " + productId);
            }

            product.setQuantity(product.getQuantity() + quantityToAdd);
        }
        log.info("Products returned");
    }

    @Override
    public BookedProductsDto assembleProducts(AssemblyProductsForOrderRequest request) {
        log.info("Assembling products for order: {}", request.getOrderId());

        BookedProductsDto bookedProductsDto = checkQuantity(new ShoppingCartDto(request.getOrderId(),
                request.getProducts()));

        Map<UUID, WarehouseProduct> productMap = repository.findAllById(request.getProducts().keySet()).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        for (Map.Entry<UUID, Integer> entry : request.getProducts().entrySet()) {
            WarehouseProduct product = productMap.get(entry.getKey());
            product.setQuantity(product.getQuantity() - entry.getValue());
        }
        repository.saveAll(productMap.values());

        OrderBooking booking = new OrderBooking();
        booking.setOrderId(request.getOrderId());
        booking.setProducts(request.getProducts());
        booking.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
        booking.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
        booking.setFragile(bookedProductsDto.getFragile());
        booking.setDeliveryId(null);
        OrderBooking saved = orderBookingRepository.save(booking);

        log.info("Booking saved: {}", saved.getBookingId());
        return bookedProductsDto;
    }

    private void verifyProductsPresence(ShoppingCartDto cartDto) {
        Set<UUID> productsInCart = cartDto.getProducts().keySet();

        Set<UUID> existingProducts = repository.findAllById(productsInCart).stream()
                .map(WarehouseProduct::getProductId)
                .collect(Collectors.toSet());

        if (productsInCart.size() != existingProducts.size()) {

            List<String> nonPresentProducts = productsInCart.stream()
                    .filter(id -> !existingProducts.contains(id))
                    .map(UUID::toString)
                    .toList();

            throw new NoSpecifiedProductInWarehouseException("Some products from cart are not present " +
                    "in the warehouse: " + nonPresentProducts);
        }
    }
}
