package ru.yandex.practicum.delivery.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.delivery.model.Delivery;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    DeliveryDto mapToDto(Delivery delivery);

    Delivery mapFromDto(DeliveryDto deliveryDto);
}
