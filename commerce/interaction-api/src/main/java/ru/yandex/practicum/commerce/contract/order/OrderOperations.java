package ru.yandex.practicum.commerce.contract.order;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.request.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.request.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderOperations {

    @GetMapping
    List<OrderDto> getUserOrders(@RequestParam String username);

    @PutMapping
    OrderDto createOrder(@RequestParam String username, @RequestBody @Valid CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto payOrder(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto failOrderPayment(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliverOrder(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto failOrderDelivery(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotalCost(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryCost(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assembleOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto failOrderAssembly(@RequestBody UUID orderId);
}
