package ru.yandex.practicum.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.request.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.request.order.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController implements OrderOperations {

    private final OrderService service;

    @Override
    public List<OrderDto> getUserOrders(String username) {
        log.info("Try get orders for username: {}", username);
        return service.getUserOrders(username);
    }

    @Override
    public OrderDto createOrder(String username, CreateNewOrderRequest request) {
        log.info("Try create order: {}; for username: {}", request, username);
        return service.createOrder(username, request);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        log.info("Try return order: {}", request);
        return service.returnOrder(request);
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        log.info("Try pay for order: {}", orderId);
        return service.payOrder(orderId);
    }

    @Override
    public OrderDto failOrderPayment(UUID orderId) {
        log.info("Try fail order payment for order: {}", orderId);
        return service.failOrderPayment(orderId);
    }

    @Override
    public OrderDto deliverOrder(UUID orderId) {
        log.info("Try deliver order: {}", orderId);
        return service.deliverOrder(orderId);
    }

    @Override
    public OrderDto failOrderDelivery(UUID orderId) {
        log.info("Try fail delivery for order: {}", orderId);
        return service.failOrderDelivery(orderId);
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        log.info("Try complete order: {}", orderId);
        return service.completeOrder(orderId);
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        log.info("Try calculate total cost for order: {}", orderId);
        return service.calculateTotalCost(orderId);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        log.info("Try calculate delivery for order: {}", orderId);
        return service.calculateDeliveryCost(orderId);
    }

    @Override
    public OrderDto assembleOrder(UUID orderId) {
        log.info("Try assemble order: {}", orderId);
        return service.assembleOrder(orderId);
    }

    @Override
    public OrderDto failOrderAssembly(UUID orderId) {
        log.info("Try fail assembly for order: {}", orderId);
        return service.failOrderAssembly(orderId);
    }
}
