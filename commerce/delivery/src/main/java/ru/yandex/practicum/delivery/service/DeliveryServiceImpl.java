package ru.yandex.practicum.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderClient;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseClient;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryState;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.exception.delivery.NoDeliveryFoundException;
import ru.yandex.practicum.commerce.request.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private static final Double BASE_COST = 5.0;
    private static final Double ADDRESS_1_MULTIPLIER = 1.0;
    private static final Double ADDRESS_2_MULTIPLIER = 2.0;
    private static final Double DEFAULT_MULTIPLIER = 1.0;
    private static final Double FRAGILITY_RATE = 0.2;
    private static final Double WEIGHT_RATE = 0.3;
    private static final Double VOLUME_RATE = 0.2;
    private static final Double ADDRESS_MISMATCH_RATE = 0.2;

    private final DeliveryRepository repository;
    private final DeliveryMapper mapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return mapper.mapToDto(repository.save(mapper.mapFromDto(deliveryDto)));
    }

    @Override
    public DeliveryDto successfulDelivery(UUID deliveryId) {
        log.info("Changing state of delivery to successful: {}", deliveryId);
        Delivery delivery = repository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("No delivery found with id " + deliveryId));

        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.deliverOrder(delivery.getOrderId());

        log.info("Delivery state changed to successful: {}", deliveryId);
        return mapper.mapToDto(delivery);
    }

    @Override
    public DeliveryDto pickDelivery(UUID deliveryId) {
        log.info("Picking products for delivery: {}", deliveryId);
        Delivery delivery = repository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("No delivery found with id " + deliveryId));

        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);

        Delivery saved = repository.save(delivery);

        warehouseClient.shipProducts(new ShippedToDeliveryRequest(saved.getOrderId(), deliveryId));

        return mapper.mapToDto(saved);
    }

    @Override
    public DeliveryDto failDelivery(UUID deliveryId) {
        log.info("Failing delivery: {}", deliveryId);
        Delivery delivery = repository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("No delivery found with id " + deliveryId));

        delivery.setDeliveryState(DeliveryState.FAILED);

        Delivery saved = repository.save(delivery);

        orderClient.failOrderDelivery(saved.getOrderId());

        return mapper.mapToDto(saved);
    }

    @Override
    public Double calculateDeliveryCost(OrderDto order) {
        log.info("Calculating delivery cost for order: {}", order.getOrderId());
        Delivery delivery = repository.findById(order.getDeliveryId())
                .orElseThrow(() -> new NoDeliveryFoundException("No delivery found with id " + order.getDeliveryId()));

        double total = BASE_COST;

        Address warehouseAddress = delivery.getFromAddress();
        Address clientAddress = delivery.getToAddress();

        if (warehouseAddress == null || clientAddress == null || order.getDeliveryWeight() == null
            || order.getFragile() == null || order.getDeliveryVolume() == null || warehouseAddress.getStreet() == null
            || clientAddress.getStreet() == null)
            throw new IllegalArgumentException("Not enough info about delivery: missing address or measurements");

        if (warehouseAddress.getStreet().contains("ADDRESS_1")) {
            total *= ADDRESS_1_MULTIPLIER;
        } else if (warehouseAddress.getStreet().contains("ADDRESS_2")) {
            total *= ADDRESS_2_MULTIPLIER;
        } else {
            total *= DEFAULT_MULTIPLIER;
        }

        if (order.getFragile()) {
            total += total * FRAGILITY_RATE;
        }

        total += order.getDeliveryWeight() * WEIGHT_RATE;
        total += order.getDeliveryVolume() * VOLUME_RATE;

        if (warehouseAddress.getStreet().trim().equalsIgnoreCase(clientAddress.getStreet().trim())) {
            total += total * ADDRESS_MISMATCH_RATE;
        }

        return total;
    }
}
