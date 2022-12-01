package com.bank.controller.response;

import com.bank.model.Account;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryResponse {
    private String accountNumber;
    private String email;
    private BigDecimal avail_bal;

    public SummaryResponse getSummaryResponse(Account account) {
        return SummaryResponse.builder().accountNumber(account.getId()).email(account.getEmail()).avail_bal(account.getAvail_bal()).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryResponse that = (SummaryResponse) o;
        return Objects.equals(accountNumber, that.accountNumber) && Objects.equals(email, that.email) && Objects.equals(avail_bal, that.avail_bal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, email, avail_bal);
    }
}
