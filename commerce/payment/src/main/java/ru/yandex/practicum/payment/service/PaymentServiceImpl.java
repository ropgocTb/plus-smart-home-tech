package ru.yandex.practicum.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderClient;
import ru.yandex.practicum.commerce.contract.shopping.store.ShoppingStoreClient;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentState;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.exception.payment.NoPaymentFoundException;
import ru.yandex.practicum.commerce.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.payment.mapper.PaymentMapper;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Double TAX = 1.1;

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    @Override
    public PaymentDto createPayment(OrderDto order) {
        log.info("Creating a payment for order: {}", order.getOrderId());

        Payment payment = new Payment();

        payment.setOrderId(order.getOrderId());
        payment.setTotalPayment(order.getTotalPrice());
        payment.setDeliveryTotal(order.getDeliveryPrice());
        payment.setFeeTotal(order.getProductPrice());
        payment.setPaymentState(PaymentState.PENDING);

        return mapper.mapToDto(repository.save(payment));
    }

    @Override
    public Double calculateTotalCostPayment(OrderDto order) {
        log.info("Calculating total cost for order: {}", order.getOrderId());

        Payment payment = repository.findById(order.getPaymentId())
                .orElseThrow(() -> new NoPaymentFoundException("No payment found for order " + order.getOrderId()));

        if (order.getDeliveryPrice() == null || order.getProductPrice() == null || order.getTotalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough info to calculate total price " +
                    "for order " + order.getOrderId());
        }

        payment.setTotalPayment(order.getTotalPrice());
        payment.setDeliveryTotal(order.getDeliveryPrice());

        Double feeTotal = payment.getTotalPayment() * TAX + payment.getDeliveryTotal();

        payment.setFeeTotal(feeTotal);

        repository.save(payment);

        return feeTotal;
    }

    //refund = successful payment ?..
    @Override
    public void refundPayment(UUID paymentId) {
        log.info("Refunding a payment: {}", paymentId);

        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("No payment with id " + paymentId));

        if (payment.getPaymentState() != PaymentState.PENDING)
            throw new IllegalArgumentException("Only pending payments can be paid");

        payment.setPaymentState(PaymentState.SUCCESS);
        repository.save(payment);

        orderClient.payOrder(payment.getOrderId());

        log.info("Refunded a payment: {}", paymentId);
    }

    @Override
    public Double calculateProductCostPayment(OrderDto order) {
        log.info("Calculating product cost for order: {}", order.getOrderId());

        double productCost = 0.0;

        for (Map.Entry<UUID, Integer> es : order.getProducts().entrySet()) {
            ProductDto productDto = shoppingStoreClient.getProduct(es.getKey());
            productCost += productDto.getPrice() * es.getValue();
        }

        return productCost;
    }

    @Override
    public void failPayment(UUID paymentId) {
        log.info("Failing a payment: {}", paymentId);

        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("No payment with id " + paymentId));

        orderClient.failOrderPayment(payment.getOrderId());

        payment.setPaymentState(PaymentState.FAILED);
        repository.save(payment);
        log.info("Failed a payment: {}", paymentId);
    }
}
