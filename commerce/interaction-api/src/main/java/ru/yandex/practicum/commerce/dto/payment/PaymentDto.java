package ru.yandex.practicum.commerce.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentDto {

    @NotNull
    private UUID paymentId;

    private Double totalPayment;
    private Double deliveryTotal;
    private Double feeTotal;
}
