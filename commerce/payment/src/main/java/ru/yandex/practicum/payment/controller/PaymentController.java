package ru.yandex.practicum.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.payment.PaymentOperations;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.payment.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentOperations {

    private final PaymentService service;

    @Override
    public PaymentDto createPayment(OrderDto order) {
        log.info("Try create payment for order: {}", order.getOrderId());
        return service.createPayment(order);
    }

    @Override
    public Double calculateTotalCostPayment(OrderDto order) {
        log.info("Try calculate total cost for order: {}", order.getOrderId());
        return service.calculateTotalCostPayment(order);
    }

    @Override
    public void refundPayment(UUID paymentId) {
        log.info("Try refund payment: {}", paymentId);
        service.refundPayment(paymentId);
    }

    @Override
    public Double calculateProductCostPayment(OrderDto order) {
        log.info("Try calculate product cost for order: {}", order.getOrderId());
        return service.calculateProductCostPayment(order);
    }

    @Override
    public void failPayment(UUID paymentId) {
        log.info("Try fail payment: {}", paymentId);
        service.failPayment(paymentId);
    }
}
