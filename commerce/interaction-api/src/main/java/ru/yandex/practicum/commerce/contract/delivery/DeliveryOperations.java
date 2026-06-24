package ru.yandex.practicum.commerce.contract.delivery;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryOperations {

    @PutMapping
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto delivery);

    @PostMapping("/successful")
    DeliveryDto successfulDelivery(@RequestBody UUID deliveryId);

    @PostMapping("/picked")
    DeliveryDto pickDelivery(@RequestBody UUID deliveryId);

    @PostMapping("/failed")
    DeliveryDto failDelivery(@RequestBody UUID deliveryId);

    @PostMapping("/cost")
    Double calculateDeliveryCost(@RequestBody @Valid OrderDto order);
}
