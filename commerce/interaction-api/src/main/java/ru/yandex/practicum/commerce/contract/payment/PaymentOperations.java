package ru.yandex.practicum.commerce.contract.payment;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;

import java.util.UUID;

public interface PaymentOperations {

    @PostMapping
    PaymentDto createPayment(@RequestBody @Valid OrderDto order);

    @PostMapping("/totalCost")
    Double calculateTotalCostPayment(@RequestBody @Valid OrderDto order);

    @PostMapping("/refund")
    void refundPayment(@RequestBody UUID paymentId);

    @PostMapping("/productCost")
    Double calculateProductCostPayment(@RequestBody @Valid OrderDto order);

    @PostMapping("/failed")
    void failPayment(@RequestBody UUID paymentId);
}
