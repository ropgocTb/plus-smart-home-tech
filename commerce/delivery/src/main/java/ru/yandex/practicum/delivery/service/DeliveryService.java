package ru.yandex.practicum.delivery.service;

import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto delivery);

    DeliveryDto successfulDelivery(UUID deliveryId);

    DeliveryDto pickDelivery(UUID deliveryId);

    DeliveryDto failDelivery(UUID deliveryId);

    Double calculateDeliveryCost(OrderDto order);
}
