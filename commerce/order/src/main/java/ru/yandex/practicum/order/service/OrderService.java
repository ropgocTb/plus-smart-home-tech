package ru.yandex.practicum.order.service;

import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.request.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.request.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> getUserOrders(String username);

    OrderDto createOrder(String username, CreateNewOrderRequest request);

    OrderDto returnOrder(ProductReturnRequest request);

    OrderDto payOrder(UUID orderId);

    OrderDto failOrderPayment(UUID orderId);

    OrderDto deliverOrder(UUID orderId);

    OrderDto failOrderDelivery(UUID orderId);

    OrderDto completeOrder(UUID orderId);

    OrderDto calculateTotalCost(UUID orderId);

    OrderDto calculateDeliveryCost(UUID orderId);

    OrderDto assembleOrder(UUID orderId);

    OrderDto failOrderAssembly(UUID orderId);
}
