package com.bank.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @Column(name = "account_id")
    private Account account;


    private Date date;

    @OneToOne
    @Column(name = "transaction_type_id")
    private TransactionType transactionType;

    private BigDecimal amount;

    private BigDecimal balance;


    public Transaction(Account account, Date date, TransactionType transactionType, BigDecimal amount, BigDecimal balance) {
        this.account = account;
        this.date = date;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
