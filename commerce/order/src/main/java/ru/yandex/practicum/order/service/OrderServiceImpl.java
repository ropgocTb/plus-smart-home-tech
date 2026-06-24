package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.delivery.DeliveryClient;
import ru.yandex.practicum.commerce.contract.payment.PaymentClient;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseClient;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.order.OrderState;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.exception.order.NoOrderFoundException;
import ru.yandex.practicum.commerce.request.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.request.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(String username) {
        log.info("Getting orders for username: {}", username);
        return repository.findByUsername(username).stream().map(mapper::mapToDto).toList();
    }

    @Override
    public OrderDto createOrder(String username, CreateNewOrderRequest request) {
        log.info("Creating new order for username: {}", username);
        BookedProductsDto bookedProductsDto = warehouseClient.checkQuantity(request.getShoppingCart());

        Order order = new Order();

        order.setShoppingCartId(request.getShoppingCart().getCartId());
        order.setProducts(request.getShoppingCart().getProducts());
        order.setState(OrderState.NEW);
        order.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
        order.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
        order.setFragile(bookedProductsDto.getFragile());
        order.setUsername(username);

        log.info("New order created: {}", order.getOrderId());
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = repository.findById(request.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + request.getOrderId()));

        order.setState(OrderState.PRODUCT_RETURNED);
        log.info("Order: {}, returned", request.getOrderId());
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto payOrder(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        //в тз нет get эндпойнта у payment, но сюда стоило бы добавить проверку что платеж по этому заказу оплачен
        if (order.getState().equals(OrderState.ON_PAYMENT)) {
            order.setState(OrderState.PAID);
            log.info("Order: {}, paid", orderId);
            return mapper.mapToDto(repository.save(order));
        }

        order.setState(OrderState.ON_PAYMENT);
        PaymentDto paymentDto = paymentClient.createPayment(mapper.mapToDto(order));
        order.setPaymentId(paymentDto.getPaymentId());
        log.info("Order: {}, on payment", orderId);
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto failOrderPayment(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        order.setState(OrderState.PAYMENT_FAILED);
        log.info("Order: {}, failed to be paid", orderId);
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto deliverOrder(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        if (order.getState() != OrderState.NEW && order.getState() != OrderState.ON_DELIVERY)
            throw new IllegalArgumentException("Cant deliver this order");

        if (order.getState() == OrderState.NEW)
            order.setState(OrderState.ON_DELIVERY);

        if (order.getState() == OrderState.ON_DELIVERY)
            order.setState(OrderState.DELIVERED);

        if (order.getDeliveryId() == null)
            throw new IllegalArgumentException("Create delivery of this order first");

        deliveryClient.pickDelivery(order.getDeliveryId());
        log.info("Order: {}, delivered", orderId);
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto failOrderDelivery(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        order.setState(OrderState.DELIVERY_FAILED);
        log.info("Order: {}, failed to be delivered", orderId);
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        order.setState(OrderState.COMPLETED);
        log.info("Order: {}, completed", orderId);
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        Double productCost = paymentClient.calculateProductCostPayment(mapper.mapToDto(order));
        order.setProductPrice(productCost);

        Double totalCost = paymentClient.calculateTotalCostPayment(mapper.mapToDto(order));
        order.setTotalPrice(totalCost);

        log.info("Counted total cost for order: {}, its: {}", orderId, totalCost);
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        Double deliveryCost = deliveryClient.calculateDeliveryCost(mapper.mapToDto(order));
        order.setDeliveryPrice(deliveryCost);
        log.info("Counted delivery cost for order: {}, its: {}", orderId, deliveryCost);
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto assembleOrder(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        order.setState(OrderState.ASSEMBLED);

        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest();
        request.setOrderId(orderId);
        request.setProducts(order.getProducts());
        warehouseClient.assembleProducts(request);

        log.info("Order: {}, assembled", orderId);
        return mapper.mapToDto(repository.save(order));
    }

    @Override
    public OrderDto failOrderAssembly(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id " + orderId));

        order.setState(OrderState.ASSEMBLY_FAILED);
        log.info("Order: {}, failed to assemble", orderId);
        return mapper.mapToDto(repository.save(order));
    }
}
