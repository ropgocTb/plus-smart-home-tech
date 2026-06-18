package ru.yandex.practicum.payment.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.payment.model.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto mapToDto(Payment payment);
}
