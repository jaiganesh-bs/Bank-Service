package com.bank.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransactionRequest {
    @DecimalMin(value = "1.0", message = "transaction amount must be greater than 1")
    private BigDecimal amount;
}
