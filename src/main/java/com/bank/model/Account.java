package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class Account {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Email
    @NotBlank(message = "email can't be empty!")
    private String email;

    @NotBlank(message = "password can't be empty!")
    private String password;

    private BigDecimal avail_bal;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
        this.avail_bal = new BigDecimal(0);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && email.equals(account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    public void credit(BigDecimal amount) {
        this.avail_bal = avail_bal.add(amount);
    }

    public void debit(BigDecimal amount) {
        this.avail_bal = avail_bal.subtract(amount);
    }
}
