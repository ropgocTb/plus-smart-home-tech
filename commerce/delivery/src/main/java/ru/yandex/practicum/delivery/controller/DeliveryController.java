package ru.yandex.practicum.delivery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.delivery.DeliveryOperations;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.delivery.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController implements DeliveryOperations {

    private final DeliveryService service;

    @Override
    public DeliveryDto createDelivery(DeliveryDto delivery) {
        log.info("Try to create a delivery for order: {}", delivery.getOrderId());
        return service.createDelivery(delivery);
    }

    @Override
    public DeliveryDto successfulDelivery(UUID deliveryId) {
        log.info("Try to valid a successful delivery: {}", deliveryId);
        return service.successfulDelivery(deliveryId);
    }

    @Override
    public DeliveryDto pickDelivery(UUID deliveryId) {
        log.info("Try to pick a delivery: {}", deliveryId);
        return service.pickDelivery(deliveryId);
    }

    @Override
    public DeliveryDto failDelivery(UUID deliveryId) {
        log.info("Try to fail a delivery: {}", deliveryId);
        return service.failDelivery(deliveryId);
    }

    @Override
    public Double calculateDeliveryCost(OrderDto order) {
        log.info("Try to calculate delivery cost for order: {}", order.getOrderId());
        return service.calculateDeliveryCost(order);
    }
}
