package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;

import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto order);

    Double calculateTotalCostPayment(OrderDto order);

    void refundPayment(UUID paymentId);

    Double calculateProductCostPayment(OrderDto order);

    void failPayment(UUID paymentId);
}
