package com.bank.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SummaryResponse {
    private String accountNumber;
    private String name;
    private BigDecimal avail_bal;

    @Override
    public String toString() {
        return "{" +
                "accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", avail_bal=" + avail_bal +
                '}';
    }
}
