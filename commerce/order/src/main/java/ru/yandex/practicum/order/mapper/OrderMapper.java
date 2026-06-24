package ru.yandex.practicum.order.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.order.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto mapToDto(Order order);
}
