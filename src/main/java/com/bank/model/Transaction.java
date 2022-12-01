package com.bank.model;


import com.bank.model.constants.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Valid
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Min(value = 1, message = "transaction amount must be greater than 1")
    private BigDecimal amount;

    @Column(name = "balance")
    private BigDecimal accountBalance;


    public Transaction(Account account, Date date, TransactionType transactionType, BigDecimal amount) {
        this.account = account;
        this.date = date;
        this.transactionType = transactionType;
        this.amount = amount;
        this.accountBalance = account.getAvail_bal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Objects.equals(account, that.account) && Objects.equals(date, that.date) && transactionType == that.transactionType && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, date, transactionType, amount);
    }
}
