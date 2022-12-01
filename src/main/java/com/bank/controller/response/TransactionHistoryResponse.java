package com.bank.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TransactionHistoryResponse {
    private String accountNumber;
    private String name;
    private BigDecimal balance;
    private List<TransactionResponse> transactions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionHistoryResponse that = (TransactionHistoryResponse) o;
        return Objects.equals(accountNumber, that.accountNumber) && Objects.equals(name, that.name) && Objects.equals(transactions, that.transactions) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, name, transactions, balance);
    }

    @Override
    public String toString() {
        return "{" +
                "accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", transactions=" + transactions +
                ", balance=" + balance +
                '}';
    }
}
