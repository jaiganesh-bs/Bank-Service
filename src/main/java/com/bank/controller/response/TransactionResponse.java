package com.bank.controller.response;

import com.bank.model.Transaction;
import lombok.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@Setter
@Getter
@Builder
@NoArgsConstructor
public class TransactionResponse {
    private long transactionId;

    private Date date;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal accountBalance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionResponse that = (TransactionResponse) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public TransactionResponse getTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .date(transaction.getDate())
                .transactionType(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .accountBalance(transaction.getAccountBalance())
                .build();
    }
}
